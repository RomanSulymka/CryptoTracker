package com.testproject.oril.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testproject.oril.domain.Cryptocurrency;
import com.testproject.oril.services.CryptoService;
import com.testproject.oril.services.PaginatedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/cryptocurrencies")
public class CryptoController {
    private static final Logger LOGGER = Logger.getLogger(CryptoController.class.getName());
    private static final String URL_ETH = "https://cex.io/api/last_price/ETH/USD";
    private static final String URL_BTC = "https://cex.io/api/last_price/BTC/USD";
    private static final String URL_XRP = "https://cex.io/api/last_price/XRP/USD";
    private static final String ETH = "ETH";
    private static final String BTC = "BTC";
    private static final String XRP = "XRP";
    private static boolean isRunning = true;

    private final CryptoService cryptoService;
    private final PaginatedService paginatedService;

    public CryptoController(CryptoService cryptoService, PaginatedService paginatedService) {
        this.cryptoService = cryptoService;
        this.paginatedService = paginatedService;
    }

    @GetMapping("/scrap")
    public void scrap() throws InterruptedException, JsonProcessingException {
        while (isRunning) {
            Cryptocurrency eth = getCryptocurrency(URL_ETH);
            Cryptocurrency btc = getCryptocurrency(URL_BTC);
            Cryptocurrency xrp = getCryptocurrency(URL_XRP);
            cryptoService.save(eth);
            cryptoService.save(btc);
            cryptoService.save(xrp);
            LOGGER.log(Level.INFO, "Scrapping data from the URL");
            TimeUnit.SECONDS.sleep(10);
        }
    }

    @GetMapping("/stop")
    public void stop() {
        isRunning = false;
        LOGGER.log(Level.INFO, "Stop scrapping data from the URL");
    }

    @GetMapping("/all")
    public List<Cryptocurrency> getAllCryptocurrency() {
        LOGGER.log(Level.INFO, "Get all cryptocurrency");
        return cryptoService.getAll();
    }

    @GetMapping("/minprice")
    public Cryptocurrency getMinPriceCryptocurrency(@RequestParam(name = "name") String currencyName) {
        LOGGER.log(Level.INFO, "Get cryptocurrency with the lowest price: " + currencyName);
        return cryptoService.getCryptocurrencyWithMinPrice(currencyName.toUpperCase(Locale.ROOT));
    }

    @GetMapping("/maxprice")
    public Cryptocurrency getHighPriceCryptocurrency(@RequestParam(name = "name") String currencyName) {
        if (ETH.equals(currencyName.toUpperCase(Locale.ROOT))
                || BTC.equals(currencyName.toUpperCase(Locale.ROOT))
                || XRP.equals(currencyName.toUpperCase(Locale.ROOT))) {

            LOGGER.log(Level.INFO, "Get cryptocurrency with the highest price: " + currencyName);
            return cryptoService.getCryptocurrencyWithHighPrice(currencyName.toUpperCase(Locale.ROOT));
        }
        throw new NoSuchElementException("Incorrect input");
    }

    @GetMapping
    public List<Cryptocurrency> getPagination(@RequestParam(name = "name") String currencyName,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        LOGGER.log(Level.INFO, "get page: " + page + " with " + size + " elements");
        return paginatedService.getRecordsWithPagination(currencyName.toUpperCase(Locale.ROOT), page, size);
    }

    @GetMapping("/csv")
    public void createCsvReport(HttpServletResponse servlet) throws IOException {
        servlet.setContentType("text/csv");
        servlet.setHeader("Content-Disposition", "attachment; file=result.csv");
        getCsvReport(servlet.getWriter(), List.of(ETH, BTC, XRP));
        LOGGER.log(Level.INFO, "CSV report created");

    }

    private void getCsvReport(PrintWriter writer, List<String> cryptocurrenciesList) throws IOException {
        writer.write("Cryptocurrency Name, Min Price, Max Price\n");
        cryptocurrenciesList
                .forEach(i -> writer.write(i + ", " + cryptoService
                        .getCryptocurrencyWithMinPrice(i)
                        .getPrice() + ", " + cryptoService
                        .getCryptocurrencyWithHighPrice(i)
                        .getPrice() + "\n"));
    }

    private Cryptocurrency getCryptocurrency(String link) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(link, String.class);
        try {
            json = mapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (json != null) {
            return Cryptocurrency.builder()
                    .cryptoName(json.path("curr1").toString().replaceAll("\"", ""))
                    .currency(json.path("curr2").toString().replaceAll("\"", ""))
                    .price(json.path("lprice").asDouble())
                    .createdAt(new Date())
                    .build();
        }
        return null;
    }
}