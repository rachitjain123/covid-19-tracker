package com.rjrks.coronavirustracking.sevices;


import com.rjrks.coronavirustracking.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class CoronaVirusDataService {

    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 RuxitSynthetic/1.0 v5051650343 t38550";
    private List<LocationStats> allStats = new ArrayList<>();

    private static String sendGET() throws IOException {
        URL obj = new URL(VIRUS_DATA_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }

            in.close();
            return response.toString();
        }
        return "GET request not worked";
    }


    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException {

        List<LocationStats> newStats = new ArrayList<>();
        Map<String, LocationStats> countryTotal = new TreeMap<>();

        String responseString = sendGET();
        StringReader csvBodyReader = new StringReader(responseString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {

            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

            int diffFromPrevDayCases = latestCases - prevDayCases;

            if (!countryTotal.containsKey(record.get("Country/Region"))) {
                LocationStats newStat = new LocationStats();
                newStat.setLatestTotalCases(0);
                newStat.setDiffFromPrevDay(0);
                newStat.setCountry(record.get("Country/Region"));
                countryTotal.put(record.get("Country/Region"), newStat);
            }

            LocationStats countryStat = countryTotal.get(record.get("Country/Region"));

            countryStat.setDiffFromPrevDay(countryStat.getDiffFromPrevDay() + diffFromPrevDayCases);
            countryStat.setLatestTotalCases(countryStat.getLatestTotalCases() + latestCases);
        }

        countryTotal.forEach((k,v) -> newStats.add(v));

        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
}
