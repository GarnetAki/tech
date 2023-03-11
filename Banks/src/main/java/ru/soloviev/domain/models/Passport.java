package ru.soloviev.domain.models;

import ru.soloviev.domain.exceptions.ClientException;

/**
 * Model of passport, contain series and number, and validate check
 */
public class Passport {
    private final String series;
    private final String number;

    /**
     * Constructor for passport
     * @param series series of new passport, 4 digits
     * @param number number of new passport, 6 digits
     * @throws ClientException if parameters have incorrect format
     */
    public Passport(String series, String number) throws ClientException {
        ValidatePassport(series, number);
        this.series = series;
        this.number = number;
    }

    /**
     * Getter for series
     * @return series of passport
     */
    public String getSeries() {
        return series;
    }

    /**
     * Getter for number
     * @return number of passport
     */
    public String getNumber() {
        return number;
    }

    private void ValidatePassport(String series, String number) throws ClientException {
        if (series.length() != 4 || number.length() != 6) throw ClientException.CreateInvalidPassport();

        if (!series.matches("^[0-9]*$")) throw ClientException.CreateInvalidPassport();

        if (!number.matches("^[0-9]*$")) throw ClientException.CreateInvalidPassport();
    }
}
