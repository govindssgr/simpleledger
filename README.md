#### To run the application
./gradlew :runLedger -Pfile=<INPUT_FILE_PATH>
######
eg)
./gradlew :runLedger -Pfile=/Users/govind/workspace/ledger/src/main/resources/sample1.txt

#### Assumptions
1) The question doesn't clearly mention if one borrower is allowed to take multiple loans from the same bank. From the examples given if seems it shouldn't be allowed. Both parallel and sequential loans are not supported. But the same borrower can take loans from different banks.
2) Assuming borrower's name as identifier of the borrower and no two borrower of the same name exist
3) Name of the bank not the borrower include SPACE

#### Note about the solution
SegmentTree to query for amount paid till an emiNumber 
This takes O(N) for every LOAN command and O(log(N)) for every BALANCE command and PAYMENT command. N - number of emis 
######The other ways I thought before choosing this approach
1) Sort PAYMENT and BALANCE queries to match the "emiNumber order" to get O(1) for these queries with added sorting complexity. But in the real world, we might have to check BALANCE for a past emiNumber, hence, I thought this solution will not work with real time problems
2) Store payments in a list and go with O(N) complexity for every BALANCE command. N - number of payments. Number of LOAN command should typically be much lesser than BALANCE command which makes it inefficient.
3) After processing all PAYMENTS commands, compute amount Paid till every emi in a O(N) operation. This is much better as it takes one time O(N) for all loans and O(1) for BALANCE QUERY. But in the real world, we can't add all payments first and query for balance.
