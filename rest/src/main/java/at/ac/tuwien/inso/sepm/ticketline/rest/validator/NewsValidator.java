package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;

public abstract class NewsValidator {

    public static final int MIN_CHARS_TITLE = 1;
    public static final int MAX_CHARS_TITLE = 100;
    public static final int MIN_CHARS_SUMMARY = 1;
    public static final int MAX_CHARS_SUMMARY = 50;
    public static final String EMPTY_ARTICLE_REGEX = ".*>\\s*[a-zA-Z0-9]+\\s*<.*";
    public static final int MAX_CHARS_TEXT = 10000;
    public static final int MAX_SIZE_IMAGE_DATA = 8000000;

    public static void validateNews(DetailedNewsDTO detailedNewsDTO) throws NewsValidationException {
        validateTitle(detailedNewsDTO);
        validateSummary(detailedNewsDTO);
        validateText(detailedNewsDTO);
        validateImageData(detailedNewsDTO);
    }

    public static void validateTitle(DetailedNewsDTO detailedNewsDTO) throws NewsValidationException {
        if (detailedNewsDTO == null) {
            throw new NewsValidationException("News validation failed, because object reference is null!");
        }

        if (detailedNewsDTO.getTitle() == null || detailedNewsDTO.getTitle().length() < MIN_CHARS_TITLE) {
            throw new NewsValidationException("News validation failed, because title contains not enough characters!");
        }

        if (detailedNewsDTO.getTitle().length() > MAX_CHARS_TITLE) {
            throw new NewsValidationException("News validation failed, because title contains too many characters!");
        }
    }

    public static void validateSummary(DetailedNewsDTO detailedNewsDTO) throws NewsValidationException {
        if (detailedNewsDTO == null) {
            throw new NewsValidationException("News validation failed, because object reference is null!");
        }

        if (detailedNewsDTO.getSummary() == null || detailedNewsDTO.getSummary().length() < MIN_CHARS_SUMMARY) {
            throw new NewsValidationException("News validation failed, because summary contains not enough characters!");
        }

        if (detailedNewsDTO.getSummary().length() > MAX_CHARS_SUMMARY) {
            throw new NewsValidationException("News validation failed, because summary contains too many characters!");
        }
    }

    public static void validateText(DetailedNewsDTO detailedNewsDTO) throws NewsValidationException {
        if (detailedNewsDTO == null) {
            throw new NewsValidationException("News validation failed, because object reference is null!");
        }

        if (detailedNewsDTO.getText() == null || !detailedNewsDTO.getText().matches(EMPTY_ARTICLE_REGEX)) {
            throw new NewsValidationException("News validation failed, because text contains not enough characters!");
        }

        if (detailedNewsDTO.getText().length() > MAX_CHARS_TEXT) {
            throw new NewsValidationException("News validation failed, because text contains too many characters!");
        }
    }

    public static void validateImageData(DetailedNewsDTO detailedNewsDTO) throws NewsValidationException {
        if (detailedNewsDTO == null) {
            throw new NewsValidationException("News validation failed, because object reference is null!");
        }

        if (detailedNewsDTO.getImageData() != null && detailedNewsDTO.getImageData().length > MAX_SIZE_IMAGE_DATA) {
            throw new NewsValidationException("News validation failed, because image data size is too huge!");
        }
    }
}
