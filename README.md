# How to build
1. git clone https://github.com/konnov-m/CreatePDF.git
2. sudo apt install texlive-full
3. sudo apt install ttf-mscorefonts-installer
4. sudo fc-cache -f
5. cd CreatePDF
6. Change directory with .tex file
7. ./gradlew jar
8. java -jar build/libs/CreatePDF.jar
