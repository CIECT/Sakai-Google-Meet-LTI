package coza.opencollab.meetings.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import coza.opencollab.meetings.converter.InstantToStringConverter;
import coza.opencollab.meetings.converter.StringToActionConverter;
import coza.opencollab.meetings.converter.StringToInstantConverter;



@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Autowired
    private StringToInstantConverter stringToInstantConverter;

    @Autowired
    private InstantToStringConverter instantToStringConverter;

    @Autowired
    private StringToActionConverter stringToActionConverter;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToInstantConverter);
        registry.addConverter(instantToStringConverter);
        registry.addConverter(stringToActionConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                        .resourceChain(false)
                ;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Bean
    @Override
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
