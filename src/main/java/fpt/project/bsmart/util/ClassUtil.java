package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.dto.activity.SectionDto;
import fpt.project.bsmart.entity.response.ClassDetailResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

public class ClassUtil {


    public static double CLASS_PERCENTAGE_FOR_FIRST_FEEDBACK = 0.5f;
    public static double CLASS_PERCENTAGE_FOR_SECOND_FEEDBACK = 0.8f;
    public static double PERCENTAGE_RANGE = 0.1f;

    public static ClassProgressTimeDto getPercentageOfClassTime(Class clazz) {
        List<TimeTable> timeTables = clazz.getTimeTables();
        Instant now = Instant.now();
        Optional<TimeTable> nearestTimeTable = timeTables.stream().filter(timeTable -> timeTable.getDate().compareTo(now) <= 0).max(new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable o1, TimeTable o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        if (nearestTimeTable.isPresent()) {
            TimeTable presentTimeTable = nearestTimeTable.get();
            Integer currentSlotNums = presentTimeTable.getCurrentSlotNums();
//            Integer numberOfSlot = clazz.getSubCourse().getNumberOfSlot();
//            double percentage = (double) currentSlotNums / (double) numberOfSlot;
//            return new ClassProgressTimeDto(currentSlotNums, BigDecimal.valueOf(percentage).setScale(2, RoundingMode.UP).doubleValue());
        }
        return null;
    }

    public static String generateCode(String code) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int codeLength = 4;
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return code + sb.toString();
    }

    public static ClassDetailResponse convertClassToClassDetailResponse(User userLogin, Class clazz) {
        ClassDetailResponse classDetailResponse = ObjectUtil.copyProperties(clazz, new ClassDetailResponse(), ClassDetailResponse.class);

        ImageDto imageDto = ConvertUtil.convertClassImageToImageDto(clazz.getClassImage());
        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
        clazz.getTimeInWeeks().forEach(timeInWeek -> {
            timeInWeekDTOS.add(ConvertUtil.convertTimeInWeekToDto(timeInWeek));
        });
        classDetailResponse.setTimeInWeeks(timeInWeekDTOS);
        classDetailResponse.setImage(imageDto);
        if (userLogin != null) {
            List<Order> orders = userLogin.getOrder();
            orders.forEach(order -> {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                orderDetails.forEach(orderDetail -> {
                    Class aClass = orderDetail.getClazz();
                    if (aClass.equals(clazz)) {
                        classDetailResponse.setPurchase(true);
                    }
                });
            });
        }
        ActivityUtil.setSectionForCourse(clazz, classDetailResponse);


        return classDetailResponse;
    }


}

