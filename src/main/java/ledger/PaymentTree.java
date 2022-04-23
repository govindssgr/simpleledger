package ledger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * SegmentTree to query for amount paid till an emiNumber
 * This takes O(N) for every LOAN command and O(log(N)) for every BALANCE command. N - number of emis
 * The other ways I thought before choosing this approach
 * 1) Sort PAYMENT and BALANCE queries to match the "emiNumber order" to get O(1) for these queries with added sorting complexity
 * But in the real world, we might have to check BALANCE for a past emiNumber, hence, I thought this solution will not work with real time problems
 * 2) Store payments in a list and go with O(N) complexity for every BALANCE command. N - number of payments
 * Number of LOAN command should typically be much lesser than BALANCE command. Hence, I preferred this.
 * 3) After processing all PAYMENTS commands, compute amount Paid till every emi in a O(N) operation.
 * This is much better as it takes one time O(N) for all payments and O(1) for BALANCE QUERY. But in the real world, we can't add all payments first and query for balance.
 */
public class PaymentTree {
    int numberOfInstallments;
    Node root;

    public PaymentTree(int numberOfInstallments, long amountToBePaid) {
        long lastEmi;
        long emi;
        if(amountToBePaid % numberOfInstallments == 0L) {
            emi = amountToBePaid / numberOfInstallments;
            lastEmi = emi;
        } else {
            emi = amountToBePaid / numberOfInstallments + 1;
            lastEmi = amountToBePaid - (numberOfInstallments - 1) * emi;
        }

        this.numberOfInstallments = numberOfInstallments;
        build(emi, lastEmi);
    }

    private void build(long emi, long lastEmi) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(0, 0, 0));
        for (int i = 1; i < numberOfInstallments; i++) {
            queue.offer(new Node(i, i, emi));
        }
        queue.offer(new Node(numberOfInstallments, numberOfInstallments, lastEmi));

        while (queue.size() > 1) {
            int size = queue.size();
            for (int i = 0; i < size / 2; i++) {
                Node left = queue.poll();
                Node right = queue.poll();
                Node parent = new Node(left, right);
                left.parent = parent;
                right.parent = parent;
                queue.offer(parent);
            }
            if (size % 2 == 1) {
                queue.offer(queue.poll());
            }
        }

        root = queue.poll();
    }

    // Note: emiNumber is 1 indexed. we'll need to add the amount to the next emiNumber.
    // emiNumber - 1 + 1 will "emiNumber" index
    public void add(int emiNumber, long amount) {
        Node node = find(root, emiNumber);
        if (node == null) {
            // Throwing an exception as the lumpsum payment need not be done and loan must have been closed by now.
            throw new RuntimeException("lumpsum payment need not be done and loan must have been closed by now");
        }
        node.value += amount;
        update(node.parent);
    }

    private Node find(Node node, int index) {
        if (node == null || node.start > index || node.end < index) {
            return null;
        }
        if (node.start == node.end) {
            return node;
        }
        Node leftResult = find(node.left, index);
        if (leftResult != null) {
            return leftResult;
        }
        return find(node.right, index);
    }

    private void update(Node node) {
        if (node == null) {
            return;
        }
        node.value = 0L;
        if (node.left != null) {
            node.value += node.left.value;
        }
        if (node.right != null) {
            node.value += node.right.value;
        }
        update(node.parent);
    }

    public long amountPaid(int emiNumber) {
        int start = 0;
        int end = emiNumber;
        return sum(start, end, root);
    }

    private long sum(int start, int end, Node node) {
        if (node == null || node.end < start || node.start > end) {
            return 0L;
        }
        if (node.start >= start && node.end <= end) {
            return node.value;
        }
        return sum(start, end, node.left) + sum(start, end, node.right);
    }

    static class Node {
        // if start == end, then it's a leaf node
        int start;
        int end;
        long value;

        Node left;
        Node right;
        Node parent;

        Node(int start, int end, long value) {
            this.start = start;
            this.end = end;
            this.value = value;
            left = null;
            right = null;
            parent = null;
        }

        Node(Node left, Node right) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("Expected non-null left and right in PaymentTree Node");
            }

            this.start = left.start;
            this.end = right.end;
            this.value = left.value + right.value;
            this.left = left;
            this.right = right;
            parent = null;
        }
    }
}
