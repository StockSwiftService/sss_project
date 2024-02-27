package org.example.stockswiftservice.domain.stock.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.stockswiftservice.domain.stock.entity.Stock;
import org.example.stockswiftservice.domain.stock.service.StockService;
import org.example.stockswiftservice.global.rs.RsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @Getter
    @AllArgsConstructor
    public static class StocksResponse {
        private final Page<StockDto> stocks;
        private final List<StockDto> stockList;
    }

    @Getter
    @AllArgsConstructor
    public static class StocksResponse2 {
        private final List<Stock> stocks;
    }
    @Data
    public class StockDto {
        private Long id;
        private String itemName;
        private Long quantity;
        private Long purchasePrice;
        private Long salesPrice;
        private String clientName;

        public StockDto(Stock stock) {
            this.id = stock.getId();
            this.itemName = stock.getItemName();
            this.quantity = stock.getQuantity();
            this.purchasePrice = stock.getPurchasePrice();
            this.salesPrice = stock.getSalesPrice();
            this.clientName = stock.getClient().getClientName();
        }
    }

    @GetMapping("")
    public RsData<StocksResponse> stocks(@RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Stock> stockPage = this.stockService.getSearchList(kw, page);
        List<StockDto> stockDtoList = stockPage.getContent().stream()
                .map(StockDto::new)
                .collect(Collectors.toList());

        Page<StockDto> stockDtoPage = new PageImpl<>(stockDtoList, stockPage.getPageable(), stockPage.getTotalElements());
        List<StockDto> stockList = this.stockService.getList().stream()
                .map(StockDto::new)
                .collect(Collectors.toList());

        return RsData.of("S-1", "다건 성공", new StocksResponse(stockDtoPage, stockList));
    }

    @Getter
    @AllArgsConstructor
    public static class StockResponse {
        private final StockDto stockDto;
    }

    @GetMapping("/{id}")
    public RsData<StockResponse> stock(@PathVariable("id") Long id) {
        Stock stock = this.stockService.getStock(id);
        StockDto stockDto = new StockDto(stock);

        return RsData.of("S-2", "단건 성공", new StockResponse(stockDto));
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
        private String clientName;
        @NotBlank
        private String itemName;
    }

    @PostMapping("/check")
    public RsData<NameResponse> checkClientName(@Valid @RequestBody NameRequest nameRequest) {
        Optional<Stock> itemName = stockService.findByItemName(nameRequest.getClientName(),nameRequest.getItemName());
        if (itemName.isPresent()) {
            return RsData.of("S-6", "중복된 품목명", new NameResponse(itemName));
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

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        // Header
        row = sheet.createRow(rowNum++);
        String[] headers = {"거래처명", "품목명", "수량", "구매단가", "판매단가"};
        for(int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Body
        for (Stock stock : list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(stock.getClient().getClientName());
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

    @GetMapping("/search")
    public RsData<StocksResponse2> searchStocks(@RequestParam("itemName") String searchText) {
        List<Stock> stocks = stockService.searchByName(searchText);
        return RsData.of("S-1", "검색 성공", new StocksResponse2(stocks));
    }
}
