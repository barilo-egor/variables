package tgb.cryptoexchange.variables.prize.review.service;

import enums.FiatCurrency;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.grpc.generated.ReviewPrizeRequest;
import tgb.cryptoexchange.grpc.generated.ReviewPrizeResponse;
import tgb.cryptoexchange.grpc.generated.ReviewPrizeValueResponse;
import tgb.cryptoexchange.grpc.generated.UpdateReviewPrizeRequest;
import tgb.cryptoexchange.variables.prize.review.entity.ReviewPrize;
import tgb.cryptoexchange.variables.prize.review.entity.ReviewPrizeValue;
import tgb.cryptoexchange.variables.prize.review.repository.ReviewPrizeRepository;
import tgb.cryptoexchange.variables.prize.review.util.EventUtil;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ReviewPrizeService {

    public final ReviewPrizeRepository reviewPrizeRepository;

    public final ReviewPrizeEventService reviewPrizeEventService;

    private final boolean isSendReviewPrizeEnabled;

    public ReviewPrizeService(ReviewPrizeRepository reviewPrizeRepository,
                              @Autowired(required = false) ReviewPrizeEventService reviewPrizeEventService,
                              @Value("${variables.review-prize.send-all-after-start:true}") boolean isSendReviewPrizeEnabled) {
        this.reviewPrizeRepository = reviewPrizeRepository;
        this.reviewPrizeEventService = reviewPrizeEventService;
        this.isSendReviewPrizeEnabled = isSendReviewPrizeEnabled;
    }

    @PostConstruct
    public void sendReviewPrizeEventAfterStart() {
        if (isSendReviewPrizeEnabled && reviewPrizeEventService != null) {
            List<ReviewPrizeResponse> reviewPrizeResponses = findAll();
            reviewPrizeEventService.process(EventUtil.mapToEvent(reviewPrizeResponses));
        }
    }

    public ReviewPrizeResponse getReviewPrize(ReviewPrizeRequest request) {
        log.debug("getReviewPrize by request: {}", request);
        return reviewPrizeRepository.findByFiatCurrency(
                        request.getFiatCurrency())
                .map(this::mapToResponse)
                .orElse(null);
    }

    public List<ReviewPrizeResponse> findAll() {
        log.debug("findAll reviewPrize");
        return reviewPrizeRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public void updateReviewPrize(UpdateReviewPrizeRequest request) {
        log.debug("updateReviewPrize by request: {}", request);
        ReviewPrize reviewPrize = reviewPrizeRepository.findByFiatCurrency(
                request.getFiatCurrency()).orElse(null);

        if (reviewPrize == null) {
            reviewPrize = new ReviewPrize();
            reviewPrize.setFiatCurrency(FiatCurrency.valueOfNullable(request.getFiatCurrency()));
        } else {
            reviewPrize.getValue().clear();
        }
        final ReviewPrize finalReviewPrize = reviewPrize;
        request.getValuesList().forEach(v -> {
            ReviewPrizeValue newValue = ReviewPrizeValue.builder()
                    .sum(v.getSum())
                    .minPrize(v.getMinPrize())
                    .maxPrize(v.getMaxPrize())
                    .reviewPrize(finalReviewPrize)
                    .build();
            finalReviewPrize.getValue().add(newValue);
        });
        reviewPrizeRepository.save(reviewPrize);

        if (reviewPrizeEventService != null) {
            reviewPrizeEventService.process(EventUtil.mapToEvent(Collections.singletonList((mapToResponse(reviewPrize)))));
        }
    }

    private ReviewPrizeResponse mapToResponse(ReviewPrize entity) {
        ReviewPrizeResponse.Builder builder = ReviewPrizeResponse.newBuilder()
                .setId(entity.getId());

        if (entity.getFiatCurrency() != null) builder.setFiatCurrency(entity.getFiatCurrency().name());

        entity.getValue().forEach(v -> builder.addValues(ReviewPrizeValueResponse.newBuilder()
                .setSum(v.getSum())
                .setMinPrize(v.getMinPrize())
                .setMaxPrize(v.getMaxPrize())
                .build()));

        return builder.build();
    }

}
