package com.company.storehouse.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.company.storehouse.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public final class CSVUtil {

    private static final String DELIMITER = ";";
    private static final Character CHAR_DELIMITER = ';';

    /**
     * This method allows to parse a waybill from CSV
     *
     * @param csvFile a waybill file in *.csv format
     * @return a list of parsed products
     */
    public static List<Product> parseFromCSV(MultipartFile csvFile) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(csvFile.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                     .withSkipLines(0)
                     .withCSVParser(getParser())
                     .build()) {

            CsvToBean<Product> toProducts = new CsvToBeanBuilder<Product>(csvReader)
                    .withType(Product.class)
                    .withMappingStrategy(mapProductData())
                    .build();

            return toProducts.parse();
        }
    }

    private static CSVParser getParser() {
        return new CSVParserBuilder()
                .withSeparator(CHAR_DELIMITER)
                .withIgnoreQuotations(true)
                .build();
    }

    private static HeaderColumnNameTranslateMappingStrategy<Product> mapProductData() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Product Name", "name");
        mapping.put("Manufacturer", "manufacturer");
        mapping.put("Weight", "weight");
        mapping.put("Produced Date", "producedAt");
        mapping.put("Expired Date", "expiredAt");

        HeaderColumnNameTranslateMappingStrategy<Product> result = new HeaderColumnNameTranslateMappingStrategy<>();
        result.setType(Product.class);
        result.setColumnMapping(mapping);
        return result;
    }

    /**
     * This method allows to write a waybill to CSV and to download a generated file
     *
     * @param writer   the PrintWriter from the HttpServletResponse
     * @param products a list of products from a waybill
     */
    public static void write(PrintWriter writer, List<Product> products) {
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]{"Product Name", "Manufacturer", "Weight", "Produced Date", "Expired Date"});

        products.stream()
                .map(product -> new String[]{
                        product.getName(),
                        product.getManufacturer(),
                        product.getWeight().toString(),
                        convertDateToString(product.getProducedAt()),
                        convertDateToString(product.getExpiredAt())
                })
                .forEach(dataLines::add);

        dataLines.stream()
                .map(CSVUtil::convertToCSV)
                .forEach(writer::println);

        writer.close();
    }

    private static String convertDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(CSVUtil::escapeSpecialCharacters)
                .collect(Collectors.joining(DELIMITER));
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains(DELIMITER) || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
