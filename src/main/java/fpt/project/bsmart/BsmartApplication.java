package fpt.project.bsmart;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Apply Default Global SecurityScheme in springdoc-openapi", version = "1.0.0"),
        security = {@SecurityRequirement(name = "Bearer Authentication")})
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class BsmartApplication {


    private final RoleRepository roleRepository;
    private final DayOfWeekRepository dayOfWeekRepository;

    public BsmartApplication(RoleRepository roleRepository, DayOfWeekRepository dayOfWeekRepository) {
        this.roleRepository = roleRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
    }


    public static void main(String[] args) throws ParseException {

        SpringApplication.run(BsmartApplication.class, args);

        int[] array = {1, 3, 8, 2, 6};
        int[] ints = insertionSort(array);
        System.out.println(Arrays.toString(ints));
    }

    private static int[] insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int currentValue = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > currentValue) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = currentValue;

        }


        return array;
    }
}





