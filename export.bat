xcopy /Y assets\*.txt export\assets\ /E
xcopy /Y src export\src\ /E

xcopy /Y compile.bat export\
xcopy /Y compile.bat export\
xcopy /Y config.properties export\
xcopy /Y pom.xml export\
xcopy /Y start.bat export\

del export\assets\movelist.txt
del export\src\main\java\za\co\entelect\competition\bots\TreeNode.java
del export\src\main\java\za\co\entelect\competition\bots\MCTS.java