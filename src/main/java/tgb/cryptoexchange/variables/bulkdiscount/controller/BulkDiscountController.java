package tgb.cryptoexchange.variables.bulkdiscount.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import tgb.cryptoexchange.grpc.generated.BulkDiscountRequest;
import tgb.cryptoexchange.grpc.generated.BulkDiscountResponse;
import tgb.cryptoexchange.grpc.generated.BulkDiscountServiceGrpc;
import tgb.cryptoexchange.grpc.generated.UpdateBulkDiscountRequest;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

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
}
