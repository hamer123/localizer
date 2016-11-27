package com.pw.localizer.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.localizer.model.dto.UserLastLocationsDTO;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Patryk on 2016-11-09.
 */

public class ReadValueTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testReadValue() throws IOException {
        String json = "{\n" +
                "    \"lastLocationNetworkNasz\": {\n" +
                "      \"network\": {\n" +
                "        \"id\": 185,\n" +
                "        \"latitude\": 51.6585053,\n" +
                "        \"longitude\": 18.92474216,\n" +
                "        \"user\": {\n" +
                "          \"id\": 2,\n" +
                "          \"login\": \"hamer123\",\n" +
                "          \"email\": \"hamer123@vp.pl\",\n" +
                "          \"phone\": \"111-111-111\"\n" +
                "        },\n" +
                "        \"date\": 1436400000000,\n" +
                "        \"providerType\": \"NETWORK\",\n" +
                "        \"address\": {\n" +
                "          \"city\": \"Łask\",\n" +
                "          \"street\": \"Kolumna 123/2\"\n" +
                "        },\n" +
                "        \"accuracy\": 0.8,\n" +
                "        \"cellInfoMobile\": {\n" +
                "          \"lte\": {\n" +
                "            \"id\": 186,\n" +
                "            \"signalStrength\": {\n" +
                "              \"assusLevel\": 123,\n" +
                "              \"dbm\": 321,\n" +
                "              \"level\": 99\n" +
                "            },\n" +
                "            \"ci\": 10,\n" +
                "            \"mcc\": 8,\n" +
                "            \"mnc\": 9,\n" +
                "            \"pci\": 7,\n" +
                "            \"tac\": 6,\n" +
                "            \"timingAdvance\": 5\n" +
                "          }\n" +
                "        },\n" +
                "        \"wifiInfo\": {\n" +
                "          \"id\": 187,\n" +
                "          \"frequency\": 123,\n" +
                "          \"bssid\": \"ala ma kota\",\n" +
                "          \"ipAddress\": 1234567890,\n" +
                "          \"linkSpeed\": 101,\n" +
                "          \"macAddress\": \"alala\",\n" +
                "          \"networkId\": 123,\n" +
                "          \"rssi\": 321,\n" +
                "          \"ssid\": \"ssid?\"\n" +
                "        },\n" +
                "        \"localizerService\": \"NASZ\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"lastLocationNetworkObcy\": {\n" +
                "      \"network\": {\n" +
                "        \"id\": 166,\n" +
                "        \"latitude\": 51.6344053,\n" +
                "        \"longitude\": 18.91674216,\n" +
                "        \"user\": {\n" +
                "          \"id\": 2,\n" +
                "          \"login\": \"hamer123\",\n" +
                "          \"email\": \"hamer123@vp.pl\",\n" +
                "          \"phone\": \"111-111-111\"\n" +
                "        },\n" +
                "        \"date\": 1436400000000,\n" +
                "        \"providerType\": \"NETWORK\",\n" +
                "        \"address\": {\n" +
                "          \"city\": \"Łask\",\n" +
                "          \"street\": \"Kolumna 123/2\"\n" +
                "        },\n" +
                "        \"accuracy\": 0.8,\n" +
                "        \"cellInfoMobile\": {\n" +
                "          \"lte\": {\n" +
                "            \"id\": 167,\n" +
                "            \"signalStrength\": {\n" +
                "              \"assusLevel\": 123,\n" +
                "              \"dbm\": 321,\n" +
                "              \"level\": 99\n" +
                "            },\n" +
                "            \"ci\": 10,\n" +
                "            \"mcc\": 8,\n" +
                "            \"mnc\": 9,\n" +
                "            \"pci\": 7,\n" +
                "            \"tac\": 6,\n" +
                "            \"timingAdvance\": 5\n" +
                "          }\n" +
                "        },\n" +
                "        \"wifiInfo\": {\n" +
                "          \"id\": 168,\n" +
                "          \"frequency\": 123,\n" +
                "          \"bssid\": \"ala ma kota\",\n" +
                "          \"ipAddress\": 1234567890,\n" +
                "          \"linkSpeed\": 101,\n" +
                "          \"macAddress\": \"alala\",\n" +
                "          \"networkId\": 123,\n" +
                "          \"rssi\": 321,\n" +
                "          \"ssid\": \"ssid?\"\n" +
                "        },\n" +
                "        \"localizerService\": \"OBCY\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"lastLocationGps\": null" +
                "    }\n" +
                "  }";

        String json2 = "{\"lastLocationNetworkNasz\":null,\"lastLocationNetworkObcy\":null,\"lastLocationGps\":{\"id\":1,\"latitude\":123.123,\"longitude\":321.321,\"user\":null,\"date\":null,\"providerType\":null,\"address\":null,\"accuracy\":0.0}}";

        json2 = "{\n" +
                "  \"lastLocationNetworkNasz\": {\n" +
                "    \"network\": {\n" +
                "      \"id\": 185,\n" +
                "      \"latitude\": 51.6585053,\n" +
                "      \"longitude\": 18.92474216,\n" +
                "      \"user\": {\n" +
                "        \"id\": 2,\n" +
                "        \"login\": \"hamer123\",\n" +
                "        \"email\": \"hamer123@vp.pl\",\n" +
                "        \"phone\": \"111-111-111\"\n" +
                "      },\n" +
                "      \"date\": 1436400000000,\n" +
                "      \"providerType\": \"NETWORK\",\n" +
                "      \"address\": {\n" +
                "        \"city\": \"Łask\",\n" +
                "        \"street\": \"Kolumna 123/2\"\n" +
                "      },\n" +
                "      \"accuracy\": 0.8,\n" +
                "      \"cellInfoMobile\": {\n" +
                "        \"lte\": {\n" +
                "          \"id\": 186,\n" +
                "          \"signalStrength\": {\n" +
                "            \"assusLevel\": 123,\n" +
                "            \"dbm\": 321,\n" +
                "            \"level\": 99\n" +
                "          },\n" +
                "          \"ci\": 10,\n" +
                "          \"mcc\": 8,\n" +
                "          \"mnc\": 9,\n" +
                "          \"pci\": 7,\n" +
                "          \"tac\": 6,\n" +
                "          \"timingAdvance\": 5\n" +
                "        }\n" +
                "      },\n" +
                "      \"wifiInfo\": {\n" +
                "        \"id\": 187,\n" +
                "        \"frequency\": 123,\n" +
                "        \"bssid\": \"ala ma kota\",\n" +
                "        \"ipAddress\": 1234567890,\n" +
                "        \"linkSpeed\": 101,\n" +
                "        \"macAddress\": \"alala\",\n" +
                "        \"networkId\": 123,\n" +
                "        \"rssi\": 321,\n" +
                "        \"ssid\": \"ssid?\"\n" +
                "      },\n" +
                "      \"localizerService\": \"NASZ\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"lastLocationNetworkObcy\": {\n" +
                "    \"network\": {\n" +
                "      \"id\": 166,\n" +
                "      \"latitude\": 51.6344053,\n" +
                "      \"longitude\": 18.91674216,\n" +
                "      \"user\": {\n" +
                "        \"id\": 2,\n" +
                "        \"login\": \"hamer123\",\n" +
                "        \"email\": \"hamer123@vp.pl\",\n" +
                "        \"phone\": \"111-111-111\"\n" +
                "      },\n" +
                "      \"date\": 1436400000000,\n" +
                "      \"providerType\": \"NETWORK\",\n" +
                "      \"address\": {\n" +
                "        \"city\": \"Łask\",\n" +
                "        \"street\": \"Kolumna 123/2\"\n" +
                "      },\n" +
                "      \"accuracy\": 0.8,\n" +
                "      \"cellInfoMobile\": {\n" +
                "        \"lte\": {\n" +
                "          \"id\": 167,\n" +
                "          \"signalStrength\": {\n" +
                "            \"assusLevel\": 123,\n" +
                "            \"dbm\": 321,\n" +
                "            \"level\": 99\n" +
                "          },\n" +
                "          \"ci\": 10,\n" +
                "          \"mcc\": 8,\n" +
                "          \"mnc\": 9,\n" +
                "          \"pci\": 7,\n" +
                "          \"tac\": 6,\n" +
                "          \"timingAdvance\": 5\n" +
                "        }\n" +
                "      },\n" +
                "      \"wifiInfo\": {\n" +
                "        \"id\": 168,\n" +
                "        \"frequency\": 123,\n" +
                "        \"bssid\": \"ala ma kota\",\n" +
                "        \"ipAddress\": 1234567890,\n" +
                "        \"linkSpeed\": 101,\n" +
                "        \"macAddress\": \"alala\",\n" +
                "        \"networkId\": 123,\n" +
                "        \"rssi\": 321,\n" +
                "        \"ssid\": \"ssid?\"\n" +
                "      },\n" +
                "      \"localizerService\": \"OBCY\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"lastLocationGps\": {\n" +
                "    \"gps\": {\n" +
                "      \"id\": 97,\n" +
                "      \"latitude\": 51.6364053,\n" +
                "      \"longitude\": 18.9024216,\n" +
                "      \"user\": {\n" +
                "        \"id\": 2,\n" +
                "        \"login\": \"hamer123\",\n" +
                "        \"email\": \"hamer123@vp.pl\",\n" +
                "        \"phone\": \"111-111-111\"\n" +
                "      },\n" +
                "      \"date\": 1436400000000,\n" +
                "      \"providerType\": \"GPS\",\n" +
                "      \"address\": {\n" +
                "        \"city\": \"Zduńska Wola\",\n" +
                "        \"street\": \"Zielona 13\"\n" +
                "      },\n" +
                "      \"accuracy\": 0.8\n" +
                "    }\n" +
                "  }\n" +
                "}";

        UserLastLocationsDTO userLastLocationsDTO = objectMapper.readValue(json, UserLastLocationsDTO.class);
        userLastLocationsDTO = objectMapper.readValue(json2, UserLastLocationsDTO.class);
    }
}
