package com.jm.online_store.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class contains settings for templates mailSendler
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel(description =  "Сущность TemplatesMailingSettings, " +
        "где хранятся название шаблона рассылки и сам текст шаблона рассылки")
public class TemplatesMailingSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_name", unique = true)
    private String settingName;

    @Column(name = "text_value")
    @Type(type = "text")
    private String textValue;

    @Column(name = "status")
    private boolean status;
}
