package com.nttdata.bootcoin.model.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "products")
public class Exchange {

    @Id
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Double purchasePrice;
    private Double salePrice;
    private String coin;

}
