package in.boimama.readstories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ContactUsRequest {

    @Schema(description = "Name of the user", minLength = 5, maxLength = 50)
    @NotNull(message = "'name' is missing in request body")
    @Size(min=5, message = "'name' should be at least 5 character")
    @Size(max=50, message = "'name' should not be greater than 50 characters")
    private String name;

    @Schema(description = "Email Id of the user")
    @NotNull(message = "'email' is missing in request body")
    @Email
    private String email;

    @Schema(description = "Phone number of the user")
    @NotNull(message = "'phone' is missing in request body")
    private String phone;

    @Schema(description = "message of the author in the contact-us form", minLength = 5, maxLength = 1000)
    @NotNull(message = "'message' is missing in request body")
    @Size(min=5, message = "'message' should be at least 4 character")
    @Size(max=1000, message = "'message' should not be greater than 1000 characters")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ContactUsRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", message='" + message + '\'' +
                '}';
    }
}
