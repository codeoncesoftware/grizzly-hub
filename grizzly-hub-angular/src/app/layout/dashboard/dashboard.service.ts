import { Injectable } from '@angular/core';
import { CHARTCONFIG } from './charts.config';
import { Analytic } from 'src/app/shared/models/Analytic';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { SubscribeModalComponent } from './subscribe-modal/subscribe-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MessageService } from '../../shared/message-modal/message.service';


@Injectable({
    providedIn: 'root'
})
export class DashboardService {
    config = CHARTCONFIG;

    private baseUrl: string = environment.baseUrl + '/api/analytics';
    private test: string = environment.baseUrl + '/api/microservice';

    constructor(
        private http: HttpClient,
        private dialog: MatDialog,
        private messageBoxService: MessageService,
        private router:Router
        ) { }


        public tryToConnect() {
            // this.messageBoxService.openInfo
            this.messageBoxService
              .openInfo(
                'messageBox.alert.interdit',
                '',
                {
                  title: '',
                  info: {
                    msg: 'Vous devez s`inscrire pour se bénéficier de cette fonctionnalité',
                    // infos: [
                    //   'messageBox.microservice.msgDeleteAllVersion',
                    //   'messageBox.microservice.msgDeleteAllFiles',
                    // ],
                  },
                }
              )
              .afterClosed()
              .subscribe((data) => {
                if (data) {
                 this.router.navigateByUrl('/sign-up')
                }
              });
          }

    // openModalSubscribe(): void {
    //     this.dialog.open(SubscribeModalComponent, {
    //       width: '1000px',
    //       height: '80vh',
    //       position: {
    //         top: '10vh',
    //         right: '20vh',
    //       },
    //     //   data: {
    //     //     microservice: ms,
    //     //     subscription: sub,
    //     //   },
    //     });
    //   }

    count(apiCounts: any, key: string): number {
        const val = apiCounts.find(a => a.name === key);
        if (val) {
            return val.count;
        }
        return 0;
    }

    public getAnalytics(): Observable<Analytic> {
        return this.http.get<Analytic>(this.baseUrl);
    }

    public getApiCountGraph(query: number, thymeleaf: number, freemarker: number, xsl: number): any {
        return {
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c} ({d}%)'
            },
            legend: {
                show: false,
                orient: 'vertical',
                x: 'right',
                data: ['Direct', 'Email', 'Affiliate', 'Video Ads', 'Search']
            },
            toolbox: {
                show: false,
                feature: {
                    restore: { show: true, title: 'restore' },
                    saveAsImage: { show: true, title: 'save as image' }
                }
            },
            calculable: true,
            series: [
                {
                    name: 'API Count',
                    type: 'pie',
                    radius: ['51%', '69%'],
                    itemStyle: {
                        normal: {
                            color: this.config.info
                        },
                        emphasis: {
                            label: {
                                show: true,
                                position: 'center',
                                textStyle: {
                                    fontSize: '20',
                                    fontWeight: 'normal'
                                }
                            }
                        }
                    },
                    data: [
                        {
                            value: query,
                            name: 'Query',
                            itemStyle: {
                                normal: {
                                    color: this.config.success,
                                    label: {
                                        textStyle: {
                                            color: this.config.success
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.success
                                        }
                                    }
                                }
                            }
                        },
                        {
                            value: xsl,
                            name: 'XSL',
                            itemStyle: {
                                normal: {
                                    color: this.config.primary,
                                    label: {
                                        textStyle: {
                                            color: this.config.primary
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.primary
                                        }
                                    }
                                }
                            }
                        },
                        {
                            value: freemarker,
                            name: 'Freemarker',
                            itemStyle: {
                                normal: {
                                    color: this.config.infoAlt,
                                    label: {
                                        textStyle: {
                                            color: this.config.infoAlt
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.infoAlt
                                        }
                                    }
                                }
                            }
                        },
                        {
                            value: thymeleaf,
                            name: 'Thymeleaf',
                            itemStyle: {
                                normal: {
                                    color: this.config.info,
                                    label: {
                                        textStyle: {
                                            color: this.config.info
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.info
                                        }
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        };

    }

    public getDataUploadChart(content: number, file: number) {
        return {
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {c} ({d}%)'
            },
            calculable: true,
            series: [
                {
                    name: 'Data',
                    type: 'pie',
                    radius: ['51%', '69%'],
                    itemStyle: {
                        normal: {
                            color: this.config.info
                        },
                        emphasis: {
                            label: {
                                show: true,
                                position: 'center',
                                textStyle: {
                                    fontSize: '20',
                                    fontWeight: 'normal'
                                }
                            }
                        }
                    },
                    data: [
                        {
                            value: content,
                            name: 'Content',
                            itemStyle: {
                                normal: {
                                    color: this.config.success,
                                    label: {
                                        textStyle: {
                                            color: this.config.success
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.success
                                        }
                                    }
                                }
                            }
                        },
                        {
                            value: file,
                            name: 'File',
                            itemStyle: {
                                normal: {
                                    color: this.config.info,
                                    label: {
                                        textStyle: {
                                            color: this.config.info
                                        }
                                    },
                                    labelLine: {
                                        lineStyle: {
                                            color: this.config.info
                                        }
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        };
    }

    public getSubscribedMicroservices() {
        return this.http.get(this.test + '/subscribed-microservices');
    }

    public search(keyword){
        return this.http.get<any>(this.test + '/search/' + keyword);
    }

}
