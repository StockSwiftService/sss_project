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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @Getter
    @AllArgsConstructor
    public static class StocksResponse {
        private final Page<Stock> stocks;
        private final List<Stock> stockList;
    }

    @GetMapping("")
    public RsData<StocksResponse> stocks(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Stock> stocks = this.stockService.getSearchList(kw, page);
        List<Stock> stockList = this.stockService.getList();

        return RsData.of("S-1", "다건 성공", new StocksResponse(stocks, stockList));
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
        private String clientName;
        @NotBlank
        private String itemName;
        @NotNull
        private Long quantity;
        @NotNull
        private Long purchasePrice;
        @NotNull
        private Long salesPrice;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private final Stock stock;
    }

    @PostMapping("")
    public RsData<CreateResponse> create(@Valid @RequestBody CreateRequest createRequest) {
        RsData<Stock> stock = stockService.create(createRequest.getClientName(), createRequest.getItemName(),
                  createRequest.getQuantity(), createRequest.getPurchasePrice(), createRequest.getSalesPrice());

        if (stock.isFail()) return (RsData) stock;

        return RsData.of(stock.getResultCode(), stock.getMsg(), new CreateResponse(stock.getData()));
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse {
        private final Stock stock;
    }

    @Data
    public static class ModifyRequest {
        @NotBlank
        private String clientNameModify;
        @NotBlank
        private String itemNameModify;
        @NotNull
        private Long purchasePriceModify;
        @NotNull
        private Long salesPriceModify;
    }

    @PatchMapping("/{id}")
    public RsData<Stock> modify(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);

        RsData<Stock> modifyStock = stockService.modify(stock,
                modifyRequest.getClientNameModify(), modifyRequest.getItemNameModify(), modifyRequest.getPurchasePriceModify(), modifyRequest.getSalesPriceModify());

        return modifyStock;
    }


    @DeleteMapping("/{id}")
    public RsData<Stock> delete(@PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);
        RsData<Stock> stockRsData = this.stockService.delete(stock);
        return stockRsData;
    }

    @PostMapping("/deleteMultiple")
    public RsData<List<Stock>> deleteMultiple(@RequestBody List<Long> id) {
        List<Stock> deletedClients = stockService.deleteMultiple(id);
        return RsData.of("S-5", "정보 삭제", deletedClients);
    }
    @AllArgsConstructor
    @Getter
    public static class NameResponse {
        private final Optional<Stock> stock;
    }

    @Data
    public static class NameRequest {
        @NotBlank
        private String itemName;
    }

    @PostMapping("/check")
    public RsData<StockController.NameResponse> checkClientName(@Valid @RequestBody NameRequest nameRequest) {
        Optional<Stock> itemName = stockService.findByItemName(nameRequest.getItemName());
        if (itemName.isPresent()) {
            return RsData.of("S-6", "중복된 품목명", new StockController.NameResponse(itemName));
        } else {
            return RsData.of("S-7", "사용 가능", null);
        }
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
        cell.setCellValue("거래처명");
        cell = row.createCell(1);
        cell.setCellValue("품목명");
        cell = row.createCell(2);
        cell.setCellValue("수량");
        cell = row.createCell(3);
        cell.setCellValue("구매단가");
        cell = row.createCell(4);
        cell.setCellValue("판매단가");

        // Body
        for (Stock stock : list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(stock.getClientName());
            cell = row.createCell(1);
            cell.setCellValue(stock.getItemName());
            cell = row.createCell(2);
            cell.setCellValue(stock.getQuantity());
            cell = row.createCell(3);
            cell.setCellValue(stock.getPurchasePrice());
            cell = row.createCell(4);
            cell.setCellValue(stock.getSalesPrice());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }
}
