package com.testproject.oril.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;
    private String cryptoName;
    private String currency;
    private Double price;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;
}
