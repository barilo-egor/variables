package tgb.cryptoexchange.variables.reviewPrize.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.util.CollectionUtils;
import tgb.cryptoexchange.grpc.generated.*;
import tgb.cryptoexchange.variables.reviewPrize.service.ReviewPrizeService;

import java.util.List;

public class ReviewPrizeController extends ReviewPrizeServiceGrpc.ReviewPrizeServiceImplBase {

    private final ReviewPrizeService reviewPrizeService;

    public ReviewPrizeController(ReviewPrizeService reviewPrizeService) {
        this.reviewPrizeService = reviewPrizeService;
    }

    @Override
    public void getReviewPrize(ReviewPrizeRequest request, StreamObserver<ReviewPrizeResponse> responseObserver) {
        ReviewPrizeResponse response = reviewPrizeService.getReviewPrize(request);
        if (response == null) {
            responseObserver.onNext(ReviewPrizeResponse.getDefaultInstance());
        } else {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateReviewPrize(UpdateReviewPrizeRequest request, StreamObserver<Empty> responseObserver) {
        reviewPrizeService.updateReviewPrize(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<FindAllReviewPrizeResponses> responseObserver) {
        List<ReviewPrizeResponse> response = reviewPrizeService.findAll();
        if (CollectionUtils.isEmpty(response)) {
            responseObserver.onNext(FindAllReviewPrizeResponses.getDefaultInstance());
        } else {
            responseObserver.onNext(FindAllReviewPrizeResponses.newBuilder().addAllValues(response).build());
        }
        responseObserver.onCompleted();
    }

}
