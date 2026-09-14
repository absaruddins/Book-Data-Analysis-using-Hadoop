@echo off
setlocal
for %%D in (mergedataset Rating_Summarization TopBooks TopBooksByPublisher) do (
  echo Building %%D...
  pushd %%D
  mvn clean package
  if errorlevel 1 (echo Build failed in %%D & popd & exit /b 1)
  popd
)
echo All modules built successfully.
endlocal
