package com.example.TexToPdf.service;

import com.example.TexToPdf.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.example.TexToPdf.utils.TexFiles.saveFileToLocalDisk;


@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    static final String CREATE_VARS = "create vars:";

    static final String CREATE_PDF = "create pdf:";

    static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasDocument()) {
            Document d = update.getMessage().getDocument();

            String fileName = d.getFileName();
            String fileId = d.getFileId();
            System.out.println(fileId);
            File file;
            try {
                file = uploadFile(fileId);
                saveFileToLocalDisk(file, "/var/files/TexToPdfBot/" + update.getMessage().getChatId() + ".tex");
            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
            }



        } else if (update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().toLowerCase().contains(CREATE_PDF)) {

            Map<String, String> mapData = new HashMap<>();
            String stringData = update.getMessage().getText().toLowerCase();
            log.info("String to parse: " + stringData);
            stringData = stringData.replace(CREATE_PDF, "");
            String[] stringArr = stringData.split(",|;|\n");

            for (int i = 0; i < stringArr.length; i++) {
                String args = stringArr[i].replace(" ", "");
                String[] splitedArgs = args.split("=");

                if (splitedArgs.length == 2 && !splitedArgs[0].isEmpty() && !splitedArgs[1].isEmpty()) {
                    mapData.put(splitedArgs[0], splitedArgs[1]);
                } else {
                    sendMessage(update.getMessage().getChatId(), "Incorrect data. Check /help.");
                    break;
                }
            }
            // Create pdf here

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/addTexFile":
                    sendMessage(chatId, "Send me file, where you want to change your variable.\n" +
                            "You should name your variable like this: VAR::surname, VAR::age, VAR::any-text");
                    break;
                case "/help":
                    sendMessage(chatId, "If you want to create pdf file You should send me .tex file. " +
                            "File can contain any variables. You MUST name vars like this: VAR::surname, VAR::age, VAR::any-text\n\n" +
                            "Then you can create pdf. You should text me message like this:\n" +
                            "Create pdf: surname=Konnov, name=Misha\n\n" +
                            "Where \"Create pdf: \" is calling a function to create pdf.\n" +
                            "Surname and name it's your variable in .tex file.\n" +
                            "Konnov and Misha are variables values.");
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
                    break;
            }
        }

    }

    private void startCommandReceived(long chatId, String firstName) {
        log.info(firstName + " start's bot with chatId = " + chatId);
        String answer = "Hi, " + firstName + ", nice to meet you!";

        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }



    public File uploadFile(String fileId) throws TelegramApiException {
        GetFile getFile = new GetFile(fileId);
        org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
        return downloadFileFromTelegram(file);
    }

    private File downloadFileFromTelegram(org.telegram.telegrambots.meta.api.objects.File file) {
        String fileUrl = getFilePath(file);
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            String fileName = Paths.get(file.getFilePath()).getFileName().toString();
            File downloadedFile = new File(fileName);
            FileUtils.copyInputStreamToFile(inputStream, downloadedFile);
            return downloadedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFilePath(org.telegram.telegrambots.meta.api.objects.File file) {
        return String.format("https://api.telegram.org/file/bot%s/%s", botConfig.getToken(), file.getFilePath());
    }

}
