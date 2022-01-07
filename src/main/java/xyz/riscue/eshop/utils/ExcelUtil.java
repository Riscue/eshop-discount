package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.Region;
import xyz.riscue.eshop.model.RegionPrice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class);

    public static void export(List<Game> gameList) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Reviews");

            writeHeaderLine(sheet);

            for (Game game : gameList) {
                writeDataLine(game, sheet);
            }

            FileOutputStream outputStream = new FileOutputStream("GamePrices.xlsx");
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void writeHeaderLine(XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum());

        Cell nameCell = row.createCell(row.getLastCellNum() + 1);
        nameCell.setCellValue("Game");

        for (Region region : Region.values()) {
            Cell cell = row.createCell(row.getLastCellNum());
            cell.setCellValue(region.name());
        }
    }

    private static void writeDataLine(Game game, XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell nameCell = row.createCell(row.getLastCellNum() + 1);
        nameCell.setCellValue(game.getName());

        List<RegionPrice> prices = game.getPrices();
        if (prices != null) {
            for (Region region : Region.values()) {
                RegionPrice regionPrice = prices.stream().filter(rp -> rp.getRegion().equals(region)).findFirst().orElse(null);
                Cell cell = row.createCell(row.getLastCellNum());
                cell.setCellValue(regionPrice == null ? "" : regionPrice.getPrice());
            }
        }
    }
}
