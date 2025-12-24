package ir.gov.tax.tpis.sdk.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.dto.ErrorResponseDto;
import ir.gov.tax.tpis.sdk.exceptions.TaxCollectionApiException;
import ir.gov.tax.tpis.sdk.exceptions.UnknownResponseException;
import ir.gov.tax.tpis.sdk.models.NonceEntity;
import ir.gov.tax.tpis.sdk.models.TokenModel;
import ir.gov.tax.tpis.sdk.properties.HttpHeadersProperties;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class HttpClient {
    private static final String BEARER = "Bearer ";
    private final Signatory signatory;
    private final ObjectMapper mapper;
    private final OkHttpClient client;
    private final TaxProperties taxProperties;
    private final HttpHeadersProperties httpHeadersProperties;

    public <T> T sendRequest(final Request request, final Request nonceRequest, final TypeReference<T> valueTypeRef) {
        final Request authenticatedRequest = getAuthenticatedRequest(request, nonceRequest);
        return sendRequest(authenticatedRequest, valueTypeRef);
    }

    public <T> T sendRequest(final Request request, final Request nonceRequest, Class<T> valueType) {
        final Request authenticatedRequest = getAuthenticatedRequest(request, nonceRequest);
        return sendRequest(authenticatedRequest, valueType);
    }

    private <T> T sendRequest(Request request, TypeReference<T> valueTypeRef) {
        try {
            final String result = send(request);
            return this.mapper.readValue(result, valueTypeRef);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T sendRequest(Request request, Class<T> valueType) {
        try {
            final String result = send(request);
            return this.mapper.readValue(result, valueType);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Request getAuthenticatedRequest(final Request request, final Request nonceRequest) {
        final String signedNonce = getSignedNonce(nonceRequest);
        return request.newBuilder().header(this.httpHeadersProperties.getAuthorizationHeaderName(), signedNonce).build();
    }

    @NotNull
    private String send(final Request request) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Request newRequest = addHeaders(request);
        final Call call = this.client.newCall(newRequest);
        log.debug("call {}", call.request().url().uri());

        try (final Response okResponse = call.execute()) {
            final String body = Objects.requireNonNull(okResponse.body()).string();
            if (okResponse.isSuccessful()) {
                return body;
            }

            throw getApiException(body, okResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            log.debug("send and receive in {} ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    private RuntimeException getApiException(final String body, final Response okResponse) {
        final ErrorResponseDto errorResponseDto;
        try {
            errorResponseDto = mapper.readValue(body, ErrorResponseDto.class);
        } catch (JsonProcessingException e) {
            return new UnknownResponseException(okResponse.code(), body);
        }
        return new TaxCollectionApiException(errorResponseDto);
    }

    private Request addHeaders(Request request) {
        Request.Builder newBuilder = request.newBuilder();
        for (Map.Entry<String, String> entry : this.httpHeadersProperties.getCustomHeaders().entrySet()) {
            newBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return newBuilder.build();
    }

    private String getSignedNonce(final Request request) {

        final NonceEntity nonceEntity = sendRequest(request, NonceEntity.class);
        final TokenModel tokenModel = TokenModel.builder()
                .nonce(nonceEntity.getNonce())
                .clientId(this.taxProperties.getMemoryId())
                .build();
        return BEARER + this.signatory.sign(serialize(tokenModel));
    }

    private String serialize(final Object dto) {
        try {
            return this.mapper.writeValueAsString(dto);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
