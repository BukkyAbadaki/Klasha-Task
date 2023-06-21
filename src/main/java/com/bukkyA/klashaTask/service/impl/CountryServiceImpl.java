package com.bukkyA.klashaTask.service.impl;

import com.bukkyA.klashaTask.payload.request.CityPopulationRequest;
import com.bukkyA.klashaTask.payload.request.CurrencyConversionRequest;
import com.bukkyA.klashaTask.payload.request.response.*;
import com.bukkyA.klashaTask.payload.request.response.payload.CapitalInfo;
import com.bukkyA.klashaTask.payload.request.response.payload.CityInfo;
import com.bukkyA.klashaTask.payload.request.response.payload.CountryInfo;
import com.bukkyA.klashaTask.payload.request.response.payload.State;
import com.bukkyA.klashaTask.payload.response.CountryResponseInfo;
import com.bukkyA.klashaTask.payload.response.CurrencyConversionResponse;
import com.bukkyA.klashaTask.service.CountryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CountryServiceImpl implements CountryService {
    ObjectMapper objectMapper = new ObjectMapper();

    /***
     *
     * @param noOfCities indicates the number of top cities by population we want to return
     * @return a list of strings that represent the top states
     * @throws IOException
     * @calls mapCities
     * using the above method to generate a Map of top cities for Italy, New Zealand and Ghana. the method then iterates
     * through the three maps for the highest population, this is then added to the final return list. this city with
     * the highest population is deleted from its Map. the process is repeated until the No of Cities required is complete
     * or the Maps are empty
     */
    @Override
    public List<String> getTopCities(int noOfCities) throws IOException {
        Map<String, String> forItaly = new HashMap<>();
        Map<String, String> forNZ = new HashMap<>();
        Map<String, String> forGhana = new HashMap<>();
        mapCities("italy", noOfCities, forItaly);
        mapCities("new zealand", noOfCities, forNZ);
        mapCities("ghana", noOfCities, forGhana);
        List<String> topCities = new LinkedList<>();
        int i = 0;
        while(i < noOfCities){
            if(forGhana.isEmpty() && forItaly.isEmpty() && forNZ.isEmpty()){
                break;
            }
            String topCity ="";
            Double topPopulation = Double.MIN_VALUE;
            int where =-1;
            for(Map.Entry<String, String> entry: forItaly.entrySet()){
                if(Double.valueOf(entry.getValue())>topPopulation){
                    topCity = entry.getKey();
                    topPopulation = Double.valueOf(entry.getValue());
                    where =1;
                }
            }
            for(Map.Entry<String, String> entry: forNZ.entrySet()){
                if(Double.valueOf(entry.getValue())>topPopulation){
                    topCity = entry.getKey();
                    topPopulation = Double.valueOf(entry.getValue());
                    where =2;
                }
            }
            for(Map.Entry<String, String> entry: forGhana.entrySet()){
                if(Double.valueOf(entry.getValue())>topPopulation){
                    topCity = entry.getKey();
                    topPopulation = Double.valueOf(entry.getValue());
                    where =3;
                }
            }
            topCities.add(topCity);
            if(where ==1){
                forItaly.remove(topCity);
            } else if (where == 2) {
                forNZ.remove(topCity);
            }else if(where ==3){
                forGhana.remove(topCity);
            }
            i++;
        }
        return topCities;
    }

    /**
     *
     * @param countryName the name of the country we want information on
     * @return CountryResponseInfo
     * @throws IOException
     * use the https://countriesnow.space/api/v0.1/countries/capital endpoint to get the capital, iso2 and iso3 information
     * use the https://countriesnow.space/api/v0.1/countries/population endpoint to get the population information for
     * the country
     * use the https://countriesnow.space/api/v0.1/countries/positions endpoint to get the Longitude and Latitude using
     * regex pattern because the variable long is a java keyword
     * use the https://countriesnow.space/api/v0.1/countries/currency endpoint to get currency information via regex pattern
     * to avoid creating a new class
     * the information is used to create a CountryResponseInfo instance, using the first entry in the array of population
     * data
     */
    @Override
    public CountryResponseInfo getCountryInformation(String countryName) throws IOException {
        String endPoint = "https://countriesnow.space/api/v0.1/countries/capital";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(endPoint);
        String jsonBody = String.format("{\"country\":\"%s\"}", countryName);
        StringEntity requestEntity =new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line ;
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        String content = sb.toString();
        CapitalRequestResponse capitalRequestResponse = objectMapper.readValue(content, CapitalRequestResponse.class);
        CapitalInfo capitalInfo = capitalRequestResponse.getData();
        endPoint = "https://countriesnow.space/api/v0.1/countries/population";
        post = new HttpPost(endPoint);
        requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        response = httpClient.execute(post);
        sb = new StringBuilder();
        inputStream = response.getEntity().getContent();
        bf= new BufferedReader(new InputStreamReader(inputStream));
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        content = sb.toString();
        CountryPopulationRequestResponse countryPopulationRequestResponse= objectMapper.readValue(content, CountryPopulationRequestResponse.class);
        CountryInfo countryInfo = countryPopulationRequestResponse.getData();
        endPoint = "https://countriesnow.space/api/v0.1/countries/positions";
        post = new HttpPost(endPoint);
        requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        response = httpClient.execute(post);
        inputStream = response.getEntity().getContent();
        sb= new StringBuilder();
        bf= new BufferedReader(new InputStreamReader(inputStream));
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        content = sb.toString();
        long longitude = 0L;
        long latitude = 0L;
        String patternString = "\"long\":\\s*(\\d+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            longitude = Long.parseLong(matcher.group(1));
        }
        patternString = "\"lat\":\\s*(\\d+)";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            latitude= Long.parseLong(matcher.group(1));
        }
        endPoint ="https://countriesnow.space/api/v0.1/countries/currency";
        post = new HttpPost(endPoint);
        requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        response = httpClient.execute(post);
        inputStream = response.getEntity().getContent();
        sb= new StringBuilder();
        bf= new BufferedReader(new InputStreamReader(inputStream));
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        content = sb.toString();
        String currency = "";
        patternString = "\"currency\":\\s*\"(\\w+)\"";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            currency = matcher.group(1);
        }
        return new CountryResponseInfo(countryInfo.getPopulationCounts()[countryInfo.getPopulationCounts().length-1].value(),
                capitalInfo.capital(), String.format("%d , %d ", longitude, latitude),currency, capitalInfo.iso2()+"&"+capitalInfo.iso3());
    }

    /**
     * @param countryName the name of country we want their states and cities
     * @return a map of country names and a list of their cities
     * @throws IOException
     * use https://countriesnow.space/api/v0.1/countries/states endpoint to get the states of a country
     * use https://countriesnow.space/api/v0.1/countries/state/cities endpoint while iterating through the states in the
     * first step, to get the list of their cities. the state becomes a key in the final map and its list of cities the
     * value of map entry
     */
    @Override
    public Map<String, List<String>> getStatesAndCities(String countryName) throws IOException {
        String endPoint  = "https://countriesnow.space/api/v0.1/countries/states";
        State[] states;
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(endPoint);
            String jsonBody = String.format("{\"country\":\"%s\"}", countryName);
            StringEntity requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            post.setEntity(requestEntity);
            try(CloseableHttpResponse response = httpClient.execute(post)) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                String content = sb.toString();
                StateRequestResponse stateRequestResponse = objectMapper.readValue(content, StateRequestResponse.class);
                states = stateRequestResponse.getData().states();
            }
        }
        Map<String, List<String>> statesAndCities = new HashMap<>();
        endPoint ="https://countriesnow.space/api/v0.1/countries/state/cities";
        for(State state: states){
            try(CloseableHttpClient newClient = HttpClientBuilder.create().build()) {
                HttpPost newPost = new HttpPost(endPoint);
                String jsonBody;
                if(!state.name().contains("Lagos")) {
                    jsonBody = String.format("{\"country\":\"%s\",\"state\":\"%s\"}", countryName, state.name());
                }else{
                    jsonBody = String.format("{\"country\":\"%s\",\"state\":\"%s\"}", countryName, state.name().split(" ")[0]);
                }
                StringEntity newRequestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                newPost.setEntity(newRequestEntity);
                try(CloseableHttpResponse newResponse = newClient.execute(newPost)) {
                    InputStream newInputStream = newResponse.getEntity().getContent();
                    BufferedReader newBF = new BufferedReader(new InputStreamReader(newInputStream));
                    StringBuilder newSB = new StringBuilder();
                    String cityLine;
                    while ((cityLine = newBF.readLine()) != null) {
                        newSB.append(cityLine);
                    }
                    String newContent = newSB.toString();
                    StateCitiesRequestResponse stateCities = objectMapper.readValue(newContent, StateCitiesRequestResponse.class);
                    statesAndCities.put(state.name(), Arrays.asList(stateCities.getData()));
                }
            }
        }
        return statesAndCities;
    }

    /**
     * @param conversionRequest contains the Country to get its currency, amount to convert and the target currency
     * @return CurrencyConversionResponse contains the currency and the converted amount
     * @throws IOException
     * use https://countriesnow.space/api/v0.1/countries/currency to get the String value of the country's currency. This
     * information is then used to search the exchange_rate.csv to get a conversion rate. if a conversion rate is found
     * the amount is converted to the target currency. If no exchange rate is found amount is left as is
     */
    @Override
    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest conversionRequest) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String endPoint ="https://countriesnow.space/api/v0.1/countries/currency";
        HttpPost post = new HttpPost(endPoint);
        String jsonBody = String.format("{\"country\":\"%s\"}", conversionRequest.country());
        StringEntity requestEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        InputStream inputStream = response.getEntity().getContent();
        StringBuilder sb= new StringBuilder();
        BufferedReader bf= new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        String content = sb.toString();
        String currency = "";
        String patternString = "\"currency\":\\s*\"(\\w+)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            currency = matcher.group(1);
        }
        inputStream = TypeReference.class.getResourceAsStream("/exchange_rate.csv");
        BigDecimal finalAmount = conversionRequest.amount();
        if(inputStream != null ) {
            content = new String(inputStream.readAllBytes());
            String[] lines = content.split(
                    "\r\n");
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].contains(currency) && lines[i].contains(conversionRequest.targetCurrency())) {
                    String[] currentLine = lines[i].split(",");
                    if (currentLine.length == 4) {
                        finalAmount = finalAmount.multiply(new BigDecimal(currentLine[3])).divide(new BigDecimal(currentLine[2]), RoundingMode.HALF_EVEN);
                    } else {
                        finalAmount = finalAmount.divide(new BigDecimal(currentLine[2]), RoundingMode.HALF_EVEN);
                    }
                }
            }
        }
        return  new CurrencyConversionResponse(currency, finalAmount);
    }

    /**
     * @param country the name of the country to get its cities
     * @param amount number of cities to get
     * @param countryMap the map to store the cities and their population
     * @throws IOException
     * use https://countriesnow.space/api/v0.1/countries/population/cities/filter to get a list of cities with the highest
     * population arranged in descending order. returned information is stored in the provided map
     */
    public void mapCities(String country, int amount, Map<String, String> countryMap) throws IOException {
        String endPoint = "https://countriesnow.space/api/v0.1/countries/population/cities/filter";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(endPoint);
        CityPopulationRequest  city = new CityPopulationRequest(amount,"desc", country);
        StringEntity requestEntity =new StringEntity(objectMapper.writeValueAsString(city), ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line ;
        while((line= bf.readLine())!= null){
            sb.append(line);
        }
        String content = sb.toString();
        TopCitiesRequestResponse topCitiesRequestResponse = objectMapper.readValue(content, TopCitiesRequestResponse.class);
        for(CityInfo cityInfo: topCitiesRequestResponse.getData()){
            countryMap.put(cityInfo.getCity(), cityInfo.getPopulationCounts()[0].value());
        }
    }
}
