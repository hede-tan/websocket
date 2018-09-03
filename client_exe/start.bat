@echo off
tasklist 
taskkill /f /im Test.exe
echo "关闭完成"

:: BatchGetAdmin
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    title 正在请求以管理员身份运行
    echo 正在请求临时管理员身份权限...
    goto UACPrompt
) else ( goto GetAdmin )
:UACPrompt
if not "%~1"=="" set file= ""%~1""
(echo Set UAC = CreateObject("Shell.Application"^)
echo UAC.ShellExecute "cmd.exe", "/c %~s0%file%", "", "runas", 1)> "%temp%\getadmin.vbs"
"%temp%\getadmin.vbs"
exit /B
:GetAdmin
if exist "%temp%\getadmin.vbs" ( del "%temp%\getadmin.vbs" )
pushd "%CD%"
CD /D "%~dp0"
:--------------------------------------
:StartCommand

set dir=%windir%\system32
set file64=jacob-1.19-x64.dll
set file86=jacob-1.19-x86.dll

if "%PROCESSOR_ARCHITECTURE%"=="x86" goto x86
if "%PROCESSOR_ARCHITECTURE%"=="AMD64" goto x64

:x64
	if exist %dir%%file64% (		
		echo 文件%dir%%file64%已存在，无需创建
	) else (
		echo 复制%dir%%file64%	
		copy jacob-1.19-x64.dll %windir%\system32
	)
	
:x86
	if exist %dir%%file86% (		
		echo 文件%dir%%file86%已存在，无需创建
	) else (
		echo 复制%dir%%file86%	
		copy jacob-1.19-x86.dll %windir%\system32
	)
	
start  Test
echo "启动完成"