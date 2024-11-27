package com.github.cs_24_sw_3_09.CMS.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // This might give problems. If it do please use @set and so on...
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsEntity {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
