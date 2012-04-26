###### DESCRIPTION

An academic project for a compilers course. The projects consists of a compiler of a stripped version of C. It outputs JVM code and uses JavaCC for the grammar and JJTree for creating an annotated parsing tree.

I probably won't be working on this project anymore, but since it was such a pain to develop (damn you teachers!! ) I decided to share it...


###### COMPILATION

Import into netbeans on windows and simply build. It will copy the resulting jar to TestFiles/ automatically and run the TESTALL.bat file to run all the tests.

###### STATUS

Since C is so vast I'll just highlight what it does:

- Arithmetic operations
- Globals
- Functions
- Conditions
- Whiles
- Arrays

What it doesn't do:

- Structs or Unions
- Pointers (supports arrays though)
- Array initializations like {1,2,3}

Of course it doesn't do a lot more things, but this is what I remember.

To test it, simply drag a <filename>.c file into TEST.bat and then run the resulting <filename>.class with java <filename>
