package org.example.stockswiftservice.domain.stock.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.service.StockService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @Getter
    @AllArgsConstructor
    public static class StocksResponse {
        private final List<Stock> stocks;
    }

    @GetMapping("")
    public RsData<StocksResponse> stocks() {
        List<Stock> stocks = this.stockService.getList();

        return RsData.of("S-1", "다건 성공", new StocksResponse(stocks));
    }

    @Getter
    @AllArgsConstructor
    public static class StockResponse {
        private final Stock stock;
    }

    @GetMapping("/{id}")
    public RsData<StockResponse> stock(@PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);

        return RsData.of("S-2", "단건 성공", new StockResponse(stock));
    }

    @Data
    public static class CreateRequest {
        @NotBlank
        private String itemCode;
        @NotBlank
        private String itemName;
        @NotBlank
        private String transactionDate;
        @NotBlank
        private String clientName;
        @NotNull
        private Long quantity;
        @NotNull
        private Long unitPrice;
        @NotNull
        private Long totalAmount;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private final Stock stock;
    }

    @PostMapping("")
    public RsData<CreateResponse> create(
            @Valid @RequestBody CreateRequest createRequest
    ) {
        RsData<Stock> stockRs = stockService.create(createRequest.getItemCode(), createRequest.getItemName(),
                createRequest.getTransactionDate(),createRequest.getClientName(),  createRequest.getQuantity(),
                createRequest.getUnitPrice(),createRequest.getTotalAmount());

        if (stockRs.isFail()) return (RsData) stockRs;

        return RsData.of(
                stockRs.getResultCode(),
                stockRs.getMsg(),
                new CreateResponse(stockRs.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse {
        private final Stock stock;
    }

    @Data
    public static class ModifyRequest {
        private String itemCode;
        private String itemName;
        private String transactionDate;
        private String clientName;
        private Long quantity;
        private Long unitPrice;
        private Long totalAmount;
    }

    @PatchMapping("/{id}")
    public RsData<Stock> modify(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);

        RsData<Stock> modifyStock = stockService.modify(stock, modifyRequest.getItemCode(), modifyRequest.getItemName(),
                modifyRequest.getTransactionDate(), modifyRequest.getClientName(), modifyRequest.getQuantity(),
                modifyRequest.getUnitPrice(), modifyRequest.getTotalAmount());

        return modifyStock;
    }


    @DeleteMapping("/{id}")
    public RsData<Stock> delete(@PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);
        RsData<Stock> stockRsData = this.stockService.delete(stock);
        return stockRsData;
    }

    @GetMapping("/excel")
    public void excelDownload(HttpServletResponse response) throws IOException {
        List<Stock> list = stockService.getList();

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("품목코드");
        cell = row.createCell(2);
        cell.setCellValue("품목명");
        cell = row.createCell(3);
        cell.setCellValue("거래일자");
        cell = row.createCell(4);
        cell.setCellValue("거래처명");
        cell = row.createCell(5);
        cell.setCellValue("수량");
        cell = row.createCell(6);
        cell.setCellValue("단가");
        cell = row.createCell(7);
        cell.setCellValue("금액합계");

        // Body
        for (Stock stock : list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(stock.getId());
            cell = row.createCell(1);
            cell.setCellValue(stock.getItemCode());
            cell = row.createCell(2);
            cell.setCellValue(stock.getItemName());
            cell = row.createCell(3);
            cell.setCellValue(stock.getTransactionDate());
            cell = row.createCell(4);
            cell.setCellValue(stock.getClientName());
            cell = row.createCell(5);
            cell.setCellValue(stock.getQuantity());
            cell = row.createCell(6);
            cell.setCellValue(stock.getUnitPrice());
            cell = row.createCell(7);
            cell.setCellValue(stock.getTotalAmount());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
