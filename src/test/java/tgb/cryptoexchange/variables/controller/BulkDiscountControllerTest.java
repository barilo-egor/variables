package tgb.cryptoexchange.variables.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.grpc.generated.BulkDiscountRequest;
import tgb.cryptoexchange.grpc.generated.BulkDiscountResponse;
import tgb.cryptoexchange.grpc.generated.BulkDiscountValueMessage;
import tgb.cryptoexchange.grpc.generated.UpdateBulkDiscountRequest;
import tgb.cryptoexchange.variables.bulkdiscount.controller.BulkDiscountController;
import tgb.cryptoexchange.variables.bulkdiscount.repository.OutboxEventRepository;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkDiscountControllerTest {

    @Mock
    private BulkDiscountService bulkDiscountService;

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private StreamObserver<BulkDiscountResponse> bulkDiscountResponseObserver;

    @Mock
    private StreamObserver<Empty> emptyResponseObserver;

    private BulkDiscountController bulkDiscountController;

    @BeforeEach
    void setUp() {
        bulkDiscountController = new BulkDiscountController(bulkDiscountService);
    }

    @Test
    void getBulkDiscount_Success() {
        BulkDiscountRequest request = BulkDiscountRequest.newBuilder().build();
        BulkDiscountResponse expectedResponse = BulkDiscountResponse.newBuilder()
                .addValues(BulkDiscountValueMessage.newBuilder().setDiscountRate("5.0").build())
                .build();

        when(bulkDiscountService.getBulkDiscount(request)).thenReturn(expectedResponse);

        bulkDiscountController.getBulkDiscount(request, bulkDiscountResponseObserver);

        verify(bulkDiscountResponseObserver).onNext(expectedResponse);
        verify(bulkDiscountResponseObserver).onCompleted();
    }

    @Test
    void getBulkDiscount_NullResponse_ReturnsDefaultInstance() {
        BulkDiscountRequest request = BulkDiscountRequest.newBuilder().build();
        when(bulkDiscountService.getBulkDiscount(request)).thenReturn(null);

        bulkDiscountController.getBulkDiscount(request, bulkDiscountResponseObserver);

        verify(bulkDiscountResponseObserver).onNext(BulkDiscountResponse.getDefaultInstance());
        verify(bulkDiscountResponseObserver).onCompleted();
    }

    @Test
    void updateBulkDiscount_Success() {
        UpdateBulkDiscountRequest request = UpdateBulkDiscountRequest.newBuilder().build();

        bulkDiscountController.updateBulkDiscount(request, emptyResponseObserver);

        verify(bulkDiscountService).updateBulkDiscount(request);
        verify(emptyResponseObserver).onNext(Empty.getDefaultInstance());
        verify(emptyResponseObserver).onCompleted();
    }

}
