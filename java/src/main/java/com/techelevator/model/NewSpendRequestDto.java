package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class NewSpendRequestDto {
    @Min(1)
    private int campaignId;
    @NotBlank
    private String requestName;
    @Positive
    private int amount;
    @NotBlank
    private String description;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public NewSpendRequestDto(int campaignId, int amount, String description, LocalDateTime endDate) {
        this.campaignId = campaignId;
        this.amount = amount;
        this.description = description;
        this.endDate = endDate;
    }

    public NewSpendRequestDto() {
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewSpendRequestDto that = (NewSpendRequestDto) o;
        return campaignId == that.campaignId && amount == that.amount && requestName.equals(that.requestName) && description.equals(that.description) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(campaignId, requestName, amount, description, endDate);
    }
}

