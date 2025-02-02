package world.hello.event_register.service.impl;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import world.hello.event_register.config.ResourceConfig;
import world.hello.event_register.exception.NotFoundException;
import world.hello.event_register.service.PdfService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {
    private final TemplateEngine templateEngine;
    private final ResourceConfig resourceConfig;
    public PdfServiceImpl(final TemplateEngine templateEngine, final ResourceConfig resourceConfig) {
        this.templateEngine = templateEngine;
        this.resourceConfig = resourceConfig;
    }
//     Generates a PDF document from Thymeleaf template and provided variables.
//     The generated PDF is returned as a byte array, to be used in the response.
    @Override
    public byte[] generatePdf(Map<String, Object> variables, String imageFileName) {
        try {
            log.info("Starting PDF generation...");
            log.info(variables.toString());
            // Generate a unique filename based on current timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = timestamp + ".pdf";
            log.info("Generated file name: {}", fileName);
            // Resolve the paths for image and PDF
            Path imagePath = resourceConfig.getDirectory().resolve(imageFileName);
            log.info("Image file path resolved: {}", imagePath);
            // Ensure the image exists
            if (!Files.exists(imagePath)) {
                log.error("Image file does not exist: {}", imagePath);
                throw new NotFoundException("Image file not found.");
            }
            log.info("Image file found: {}", imagePath);
            // Add image URL to Thymeleaf variables
            variables.put("imageUrl", imagePath.toUri().toString());
            log.info("Added image URL to variables: {}", imagePath.toUri());
            // Prepare the Thymeleaf context
            Context ctx = new Context();
            ctx.setVariables(variables);
            log.info("Variables passed to Thymeleaf context: {}", variables);
            // Process the Thymeleaf template
            String processedHtml = templateEngine.process("badge", ctx);
            log.info("HTML processed for PDF generation.");
            // Initialize PDF renderer
            try (ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(processedHtml);
                renderer.layout();
                renderer.createPDF(pdfOutputStream, false);
                renderer.finishPDF();
                log.info("PDF successfully created.");
                // Return the generated PDF as a byte array
                return pdfOutputStream.toByteArray();
            }
        } catch (FileNotFoundException ex) {
            log.error("File not found: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Directory or file not found.");
        } catch (IOException ex) {
            log.error("IOException occurred: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate PDF.");
        } catch (DocumentException ex) {
            log.error("DocumentException occurred: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing PDF.");
        } catch (NotFoundException ex) {
            log.error("NotFoundException: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + ex.getMessage());
        }
    }
}
