package com.velundkvz.data.model;

import java.net.URL;
import java.util.Date;

import com.velundkvz.exceptions.InvalidCouponBuildException;
import com.velundkvz.exceptions.InvalidCouponParametersException;

import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_ID_EXC_MSG;
import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_COUPON_COMPANY_ID_EXC_MSG;
import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_COUPON_AMOUNT_EXC_MSG;
import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_COUPON_PRICE_EXC_MSG;

public class Coupon
{
    private long id;
    private long company_id;
    private  String category;
    private String title;
    private long price;
    private Date start_date;
    private Date end_date;
    private int amount;
    private String description;
    private URL image_url;

    public Coupon(CouponBuilder cb) {
        this.id = cb.id;
        this.company_id = cb.company_id;
        this.category = cb.category;
        this.title = cb.title;
        this.price = cb.price;
        this.start_date = cb.start_date;
        this.end_date = cb.end_date;
        this.amount = cb.amount;
        this.description = cb.description;
        this.image_url = cb.image_url;
    }

    private void validate() throws InvalidCouponParametersException
    {
            validateId();
            validatecompanyId();
            validatePrice();
            validateAmount();
    }

    private void validatePrice()
    {
        if (price < 0)
        {
            throw new InvalidCouponParametersException(INVALID_COUPON_PRICE_EXC_MSG);
        }
    }

    private void validateAmount()
    {
        if (amount < 0)
        {
            throw new InvalidCouponParametersException(INVALID_COUPON_AMOUNT_EXC_MSG);
        }
    }

    private void validatecompanyId()
    {
        if (company_id < 0)
        {
            throw new InvalidCouponParametersException(INVALID_COUPON_COMPANY_ID_EXC_MSG);
        }
    }

    private void validateId()
    {
        if (id < 0)
        {
            throw new InvalidCouponParametersException(INVALID_ID_EXC_MSG);
        }
    }

    public static class CouponBuilder
    {
        private long id;
        private long company_id;
        private  String category;
        private String title;
        private long price;
        Date start_date;
        Date end_date;
        int amount;
        String description;
        URL image_url;
        public CouponBuilder(){}
        public CouponBuilder id(long id)
        {
            this.id = id;
            return this;
        }
        public CouponBuilder company_id(long company_id)
        {
            this.company_id = company_id;
            return this;
        }
        public CouponBuilder category(String category)
        {
            this.category = category;
            return this;
        }
        public CouponBuilder title(String title)
        {
            this.title = title;
            return this;
        }
        public CouponBuilder price(long price)
        {
            this.price = price;
            return this;
        }
        public CouponBuilder start_date(Date start_date)
        {
            this.start_date = start_date;
            return this;
        }
        public CouponBuilder end_date(Date end_date)
        {
            this.end_date = end_date;
            return this;
        }
        public CouponBuilder amount(int amount)
        {
            this.amount = amount;
            return this;
        }
        public CouponBuilder description(String description)
        {
            this.description = description;
            return this;
        }
        public CouponBuilder image_url(URL image_url)
        {
            this.image_url = image_url;
            return this;
        }

        public Coupon build()
        {
            Coupon coupon = new Coupon(this);
            try
            {
                coupon.validate();
            } catch (InvalidCouponParametersException e)
            {
                throw new InvalidCouponBuildException(e.getMessage());
            }
            return coupon;
        }

    }

    public long getId() {
        return id;
    }

    public long getCompany_id() {
        return company_id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public long getPrice() {
        return price;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public URL getImage_url() {
        return image_url;
    }
}
