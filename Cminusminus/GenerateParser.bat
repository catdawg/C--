@echo off

echo -----------JJTREE--------------

call jjtree -NODE_EXTENDS:CustomSimpleNode -OUTPUT_DIRECTORY:src/ParserGeneration src/ParserGeneration/c2jvm.jjt

echo -----------END-----------
echo ------------JAVACC-----------------

call javacc -OUTPUT_DIRECTORY:src/ParserGeneration src/ParserGeneration/c2jvm.jj

echo -----------END-----------

echo -----------DELETING .JJ FILE-----------
call del .\\src\\ParserGeneration\\c2jvm.jj
echo -----------END-----------

echo ------------MOVING ALL PARSER FILES TO CODE GENERATION-----------

call move /Y .\\src\\ParserGeneration\\*.java .\\src\\CodeGeneration\\Parser
echo -----------END-----------

