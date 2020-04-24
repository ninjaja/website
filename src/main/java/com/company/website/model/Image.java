package com.company.website.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */

@Entity
@EqualsAndHashCode
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @NotNull
    @Size(min = 2, max = 30)
    @Getter
    @Setter
    private String title;

    @Lob
    @Getter
    @Setter
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Getter
    @Setter
    private Project project;

    public Image(String title, byte[] data, Project project) {
        this.title = title;
        this.data = data;
        this.project = project;
    }

    public Image() {
    }

    public String generateBase64Image()
    {
        return Base64.encodeBase64String(this.getData());
    }
}
