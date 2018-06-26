package at.ac.tuwien.inso.sepm.ticketline.server.unittests.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.NewsValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class NewsValidatorTest {

    @Test
    public void validateValidTitle() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setTitle("A valid news title");
        NewsValidator.validateTitle(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateNullTitleShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setTitle(null);
        NewsValidator.validateTitle(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateEmptyTitleShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setTitle("");
        NewsValidator.validateTitle(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateTooLongTitleShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setTitle("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
        NewsValidator.validateTitle(detailedNewsDTO);
    }

    @Test
    public void validateValidSummary() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setSummary("A valid news summary!");
        NewsValidator.validateSummary(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateNullSummaryShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setSummary(null);
        NewsValidator.validateSummary(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateEmptySummaryShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setSummary("");
        NewsValidator.validateSummary(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateTooLongSummaryShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setSummary("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
        NewsValidator.validateSummary(detailedNewsDTO);
    }

    @Test
    public void validateValidText() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setText("<html><head></head><body contenteditable=\"true\"><p>Debitis amet dolorem saepe consequatur fuga in.</p></body></html>");
        NewsValidator.validateText(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateNullTextShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setText(null);
        NewsValidator.validateText(detailedNewsDTO);
    }

    @Test(expected = NewsValidationException.class)
    public void validateEmptyTextShouldThrowNewsValidationException() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setText("");
        NewsValidator.validateText(detailedNewsDTO);
    }

    @Test
    public void validateValidImageData() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setImageData(new byte[] { 1 });
        NewsValidator.validateImageData(detailedNewsDTO);
    }

    @Test
    public void validateNullImageData() throws NewsValidationException {
        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();
        detailedNewsDTO.setImageData(null);
        NewsValidator.validateImageData(detailedNewsDTO);
    }
}
