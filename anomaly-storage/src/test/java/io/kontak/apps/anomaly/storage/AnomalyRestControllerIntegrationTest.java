package io.kontak.apps.anomaly.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class AnomalyRestControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AnomalyRepository anomalyRepository;

    @BeforeEach
    void init() {
        anomalyRepository.deleteAll();
    }

    @Test
    void shouldReturnAnomaliesPerThermometerId() throws Exception {
        String thermometerId = "52a32c7d-9755-4dcc-a910-a1c76799501d";

        anomalyRepository.save(new AnomalyDocument("id1", 23.5, "roomId1", thermometerId, Instant.parse("2023-08-22T07:56:07.547Z")));
        anomalyRepository.save(new AnomalyDocument("id2", 16.7, "roomId1", thermometerId, Instant.parse("2023-08-22T07:56:17.547Z")));

        MvcResult mvcResult = mvc.perform(get("/anomalies/search/findByThermometerId?thermometerId=%s".formatted(thermometerId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = StringUtils.trimAllWhitespace(mvcResult.getResponse().getContentAsString());

        String expectedResponse = StringUtils.trimAllWhitespace("""
                {
                  "_embedded" : {
                    "anomalies" : [ {
                      "temperature" : 23.5,
                      "roomId" : "roomId1",
                      "thermometerId" : "52a32c7d-9755-4dcc-a910-a1c76799501d",
                      "timestamp" : "2023-08-22T07:56:07.547Z",
                      "_links" : {
                        "self" : {
                          "href" : "http://localhost/anomalies/id1"
                        },
                        "anomalyDocument" : {
                          "href" : "http://localhost/anomalies/id1"
                        }
                      }
                    }, {
                      "temperature" : 16.7,
                      "roomId" : "roomId1",
                      "thermometerId" : "52a32c7d-9755-4dcc-a910-a1c76799501d",
                      "timestamp" : "2023-08-22T07:56:17.547Z",
                      "_links" : {
                        "self" : {
                          "href" : "http://localhost/anomalies/id2"
                        },
                        "anomalyDocument" : {
                          "href" : "http://localhost/anomalies/id2"
                        }
                      }
                    } ]
                  },
                  "_links" : {
                    "self" : {
                      "href" : "http://localhost/anomalies/search/findByThermometerId?thermometerId=52a32c7d-9755-4dcc-a910-a1c76799501d"
                    }
                  }
                }""");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnAnomaliesPerRoomId() throws Exception {
        String roomId = "e0f6799b-5a4f-4d77-ac8f-5b5ff9b05783";

        anomalyRepository.save(new AnomalyDocument("id3", 13.5, roomId, "thermometerId1", Instant.parse("2023-08-22T07:57:07.547Z")));
        anomalyRepository.save(new AnomalyDocument("id4", 26.7, roomId, "thermometerId1", Instant.parse("2023-08-22T07:06:17.547Z")));

        MvcResult mvcResult = mvc.perform(get("/anomalies/search/findByRoomId?roomId=%s".formatted(roomId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = StringUtils.trimAllWhitespace(mvcResult.getResponse().getContentAsString());

        String expectedResponse = StringUtils.trimAllWhitespace("""
                {
                  "_embedded" : {
                    "anomalies" : [ {
                      "temperature" : 13.5,
                      "roomId" : "e0f6799b-5a4f-4d77-ac8f-5b5ff9b05783",
                      "thermometerId" : "thermometerId1",
                      "timestamp" : "2023-08-22T07:57:07.547Z",
                      "_links" : {
                        "self" : {
                          "href" : "http://localhost/anomalies/id3"
                        },
                        "anomalyDocument" : {
                          "href" : "http://localhost/anomalies/id3"
                        }
                      }
                    }, {
                      "temperature" : 26.7,
                      "roomId" : "e0f6799b-5a4f-4d77-ac8f-5b5ff9b05783",
                      "thermometerId" : "thermometerId1",
                      "timestamp" : "2023-08-22T07:06:17.547Z",
                      "_links" : {
                        "self" : {
                          "href" : "http://localhost/anomalies/id4"
                        },
                        "anomalyDocument" : {
                          "href" : "http://localhost/anomalies/id4"
                        }
                      }
                    } ]
                  },
                  "_links" : {
                    "self" : {
                      "href" : "http://localhost/anomalies/search/findByRoomId?roomId=e0f6799b-5a4f-4d77-ac8f-5b5ff9b05783"
                    }
                  }
                }""");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getThermometersWithAnomaliesMoreThan() throws Exception {
        int threshold = 3;

        anomalyRepository.save(new AnomalyDocument("4", 11.5, "roomId2", "thermometerId4", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("5", 21.5, "roomId3", "thermometerId5", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("6", 21.4, "roomId3", "thermometerId5", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("7", 20.4, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("8", 25.6, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("9", 18.9, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("10", 19.7, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("11", 20.3, "roomId4", "thermometerId7", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("12", 21.5, "roomId4", "thermometerId7", Instant.now()));

        MvcResult mvcResult = mvc.perform(get("/anomalies/search/findThermometersWithMoreAnomaliesThanThreshold?threshold=%d".formatted(threshold))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String actualResponse = StringUtils.trimAllWhitespace(mvcResult.getResponse().getContentAsString());

        String expectedResponse = StringUtils.trimAllWhitespace("""
                {
                    "_embedded": {
                        "thermometers": [
                            {
                                "thermometerId": "thermometerId6",
                                "anomaliesCount": 4,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost/thermometer/thermometerId6"
                                    },
                                    "thermometer": {
                                        "href": "http://localhost/thermometer/thermometerId6"
                                    }
                                }
                            }
                        ]
                    },
                    "_links": {
                        "self": {
                            "href": "http://localhost/anomalies/search/findThermometersWithMoreAnomaliesThanThreshold?threshold=3"
                        }
                    }
                }
                """);
        assertEquals(expectedResponse, actualResponse);
    }


}
