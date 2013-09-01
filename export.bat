xcopy /Y assets\*.txt export\assets\ /E
del export\assets\movelist.txt
xcopy /Y src export\src\ /E

xcopy /Y compile.bat export\
xcopy /Y compile.bat export\
xcopy /Y config.properties export\
xcopy /Y pom.xml export\
xcopy /Y start.bat export\