# How to build
1. git clone https://github.com/konnov-m/CreatePDF.git
2. sudo setfacl -R u:YOUR_USERNAME:rwx /var/log
3. sudo mkdir -p /var/files/TexToPdfBot
4. sudo setfacl -R u:YOUR_USERNAME:rwx /var/files
5. sudo apt install texlive-full
6. sudo apt install ttf-mscorefonts-installer
7. sudo fc-cache -f
8. cd CreatePDF
9.  Change bot.name and bot.token in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
9. ./gradlew bootJar
10. java -jar build/libs/TexToPdf.jar

# Суть проекта
Это телеграм бот, в который можно отправить .tex-файл и получить .pdf. Компиляция в pdf происходит с помощью инструментов texlive. Можно не просто скомпиляровать pdf, но также вставить туда свои переменные (имя, фамилия, номер телефона и др.), это позволяет создавать похожие pdf-файлы легко и быстро.
