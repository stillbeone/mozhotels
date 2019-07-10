package mozhotels.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import mozhotels.domain.enumeration.PaymentType;

import mozhotels.domain.enumeration.PaymentState;

import mozhotels.domain.enumeration.CardType;

/**
 * A BookingPayment.
 */
@Entity
@Table(name = "booking_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bookingpayment")
public class BookingPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PaymentState state;

    @NotNull
    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private Integer cardNumber;

    @NotNull
    @Column(name = "card_expiry", nullable = false)
    private LocalDate cardExpiry;

    @NotNull
    @Column(name = "card_ccv", nullable = false)
    private Integer cardCcv;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime createDate;

    @NotNull
    @Column(name = "edit_date", nullable = false)
    private ZonedDateTime editDate;

    @Column(name = "approval")
    private Boolean approval;

    @OneToOne
    @JoinColumn(unique = true)
    private Booking booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(LocalDate cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public Integer getCardCcv() {
        return cardCcv;
    }

    public void setCardCcv(Integer cardCcv) {
        this.cardCcv = cardCcv;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(ZonedDateTime editDate) {
        this.editDate = editDate;
    }

    public Boolean isApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingPayment bookingPayment = (BookingPayment) o;
        if(bookingPayment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bookingPayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingPayment{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", currency='" + currency + "'" +
            ", amount='" + amount + "'" +
            ", state='" + state + "'" +
            ", cardHolder='" + cardHolder + "'" +
            ", cardType='" + cardType + "'" +
            ", cardNumber='" + cardNumber + "'" +
            ", cardExpiry='" + cardExpiry + "'" +
            ", cardCcv='" + cardCcv + "'" +
            ", createDate='" + createDate + "'" +
            ", editDate='" + editDate + "'" +
            ", approval='" + approval + "'" +
            '}';
    }
}
