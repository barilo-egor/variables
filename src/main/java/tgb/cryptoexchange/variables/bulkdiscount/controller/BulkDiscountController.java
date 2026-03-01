package tgb.cryptoexchange.variables.bulkdiscount.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tgb.cryptoexchange.grpc.generated.*;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

import java.util.List;

@Service
public class BulkDiscountController extends BulkDiscountServiceGrpc.BulkDiscountServiceImplBase {

    private final BulkDiscountService bulkDiscountService;

    public BulkDiscountController(BulkDiscountService bulkDiscountService) {
        this.bulkDiscountService = bulkDiscountService;
    }

    @Override
    public void getBulkDiscount(BulkDiscountRequest request, StreamObserver<BulkDiscountResponse> responseObserver) {
        BulkDiscountResponse response = bulkDiscountService.getBulkDiscount(request);
        if (response == null) {
            responseObserver.onNext(BulkDiscountResponse.getDefaultInstance());
        } else {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateBulkDiscount(UpdateBulkDiscountRequest request, StreamObserver<Empty> responseObserver) {
        bulkDiscountService.updateBulkDiscount(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<FindAllMessage> responseObserver) {
        List<BulkDiscountResponse> response = bulkDiscountService.findAll();
        if (CollectionUtils.isEmpty(response)) {
            responseObserver.onNext(FindAllMessage.getDefaultInstance());
        } else {
            responseObserver.onNext(FindAllMessage.newBuilder().addAllValues(response).build());
        }
        responseObserver.onCompleted();
    }

}
