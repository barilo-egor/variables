package tgb.cryptoexchange.variables.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tgb.cryptoexchange.grpc.generated.*;
import tgb.cryptoexchange.variables.bulkdiscount.controller.BulkDiscountController;
import tgb.cryptoexchange.variables.bulkdiscount.service.BulkDiscountService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkDiscountControllerTest {

    @Mock
    private BulkDiscountService bulkDiscountService;

    @Mock
    private StreamObserver<BulkDiscountResponse> bulkDiscountResponseObserver;

    @Mock
    private StreamObserver<Empty> emptyResponseObserver;

    @Mock
    private StreamObserver<DiscountRate> discountRateResponseObserver;

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

    @Test
    void getDiscount_Success() {
        BulkDiscountWithSumRequest request = BulkDiscountWithSumRequest.newBuilder().setSum("1000").build();
        BigDecimal expectedDiscount = new BigDecimal("2.5");
        when(bulkDiscountService.getDiscount(request)).thenReturn(expectedDiscount);

        ArgumentCaptor<DiscountRate> captor = ArgumentCaptor.forClass(DiscountRate.class);

        bulkDiscountController.getDiscount(request, discountRateResponseObserver);

        verify(discountRateResponseObserver).onNext(captor.capture());
        verify(discountRateResponseObserver).onCompleted();

        DiscountRate capturedRate = captor.getValue();
        assertEquals("2.5", capturedRate.getDiscountRate());
    }
}
