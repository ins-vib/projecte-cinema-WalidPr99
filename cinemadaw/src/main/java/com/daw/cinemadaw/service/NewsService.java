package com.daw.cinemadaw.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.daw.cinemadaw.domain.cinema.New;

@Service
public class NewsService {

    public ArrayList<New> getNews() {

        ArrayList<New> news = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("news.txt");

        if (!resource.exists()) {
            return news;
        }

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(":", 2);

                if (fields.length == 2) {
                    news.add(new New(fields[0].trim(), fields[1].trim()));
                }
            }

            return news;
        } catch (IOException exception) {
            throw new IllegalStateException("No s'han pogut carregar les noticies.", exception);
        }
    }
}


