# How to build
1. git clone https://github.com/konnov-m/CreatePDF.git
2. sudo setfacl -R u:YOUR_USERNAME:rwx /var/log
3. sudo mkdir -p /var/files/TexToPdfBot
4. sudo setfacl -R u:YOUR_USERNAME:rwx /var/files
5. sudo apt install texlive-full
6. sudo apt install ttf-mscorefonts-installer
7. sudo fc-cache -f
8. cd CreatePDF
9. ��������� bot.name � bot.token � [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
10. ./gradlew bootJar
11. java -jar build/libs/TexToPdf.jar
