@echo off
echo Processing PNG files in current directory and assets subfolder...
echo.

:: Process PNG files in current directory
for %%f in (*.png) do (
    echo Processing: "%%f"
    "C:\Users\USER\Creative\OxiPNG\oxipng" -o max --alpha --strip all -Z "%%f"
)

:: Process PNG files in assets folder and all subfolders
for /r ".\assets" %%f in (*.png) do (
    echo Processing: "%%f"
    "C:\Users\USER\Creative\OxiPNG\oxipng" -o max --alpha --strip all -Z "%%f"
)

PAUSE