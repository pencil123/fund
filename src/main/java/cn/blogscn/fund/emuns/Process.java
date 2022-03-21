package cn.blogscn.fund.emuns;

public enum Process {
    IndexList(1, "指标列表更新"), IndexRecord(2, "指标数据更新"), BankuaiList(3, "行业板块列表更新"), BankuaiRecord(4,
            "行业板块数据更新"), GainianList(5, "概念板块列表更新"), GainianRecord(6, "概念板块数据更新"), FundList(7,
            "基金列表更新"), FundRecord(8, "基金数据更新"), IndexFundList(9, "指数基金列表更新"), IndexFundRecord(10,
            "指数基金数据更新"), SendMail(200, "发送邮件");
    private Integer code;
    private String step;

    Process(Integer code, String step) {
        this.code = code;
        this.step = step;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "Process{" +
                "code=" + code +
                ", step='" + step + '\'' +
                '}';
    }
}
