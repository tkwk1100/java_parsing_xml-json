package com.publicdata.ncs_info.vo;

import lombok.Data;

@Data
public class NcsInfoVO {
    private Integer ns_seq;
    private String ns_code;
    private String ns_l_class;
    private String ns_m_class;
    private String ns_s_class;
    private String ns_d_class;
    private String ns_name;
    private String ns_def;
    private Integer ns_level;
}
