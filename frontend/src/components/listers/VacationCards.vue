<template>
    <div>



            <VacationStatusQuery @search="search"></VacationStatusQuery>


        <h1 style = "margin-left:4.5%; margin-top:-10px;">Vacation</h1>
        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                        v-model="openDialog"
                        width="332.5"
                        fullscreen
                        hide-overlay
                        transition="dialog-bottom-transition"
                >
                    <Vacation :offline="offline" class="video-card" :isNew="true" :editMode="true" v-model="newValue" 
                            @add="append" v-if="tick"/>

                    <v-btn
                            style="postition:absolute; top:2%; right:2%"
                            @click="closeDialog()"
                            depressed
                            icon 
                            absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>

                <v-btn  color="blue" fab dark large
                        style="position:fixed; bottom: 5%; right: 2%; z-index:99"
                        @click="openDialog=true;"
                >
                    <v-icon>mdi-plus</v-icon>
                </v-btn>
            </div>
        </v-col>
        <v-row>
            <Vacation :offline="offline" class="video-card" v-for="(value, index) in values" v-model="values[index]" v-bind:key="index" @delete="remove"/>
        </v-row>
    </div>
</template>

<script>
    import { RSocketClient } from 'rsocket-core';
    import RSocketWebSocketClient from 'rsocket-websocket-client';
    import { IdentitySerializer, JsonSerializer } from "rsocket-core/build";

    const axios = require('axios').default;
    import Vacation from './../Vacation.vue';

    export default {
        name: 'VacationManager',
        components: {
            Vacation,
        },
        props: {
            offline: Boolean
        },
        data: () => ({
            values: [],
            newValue: {},
            tick : true,
            openDialog : false,
        }),
        async created() {
            //await this.search();

            // const rsocket = await this.createClient({
            //     host: 'https://7000-jinyoung-humanresourcem-gu87xqf8hmd.ws-us78.gitpod.io',
            //     port: 7000,
            // });

            // alert(rsocket);

            const transport = new RSocketWebSocketClient(
                {
                    url: `wss://7000-jinyoung-humanresourcem-gu87xqf8hmd.ws-us78.gitpod.io/rsocket`
                }
            );
            const client = new RSocketClient({
                // send/receive JSON objects instead of strings/buffers
                serializers: {
                data: JsonSerializer,
                metadata: IdentitySerializer
                },
                setup: {
                // ms btw sending keepalive to server
                keepAlive: 60000,
                // ms timeout if no keep-alive response
                lifetime: 180000,
                dataMimeType: "application/json",
                metadataMimeType: 'message/x.rsocket.routing.v0'
                },
                transport
            });
            client.connect().subscribe({
                onComplete: socket => {
                let requestedMsg = 10;
                let processedMsg = 0;

                // console.log("connected to rsocket"); // debug
                const endpoint = "vacations.1.get"
                socket.requestStream({
                    data: {},
                    metadata: String.fromCharCode(endpoint.length) + endpoint
                })
                    .subscribe({
                        /*
                        we create an infinite stream and to do so, in the callback
                        of the onNext event we request new messages every time
                        the client received the previously requested n messages
                        */
                        onSubscribe: (sub) => {
                            console.log("subscribed to server stream"); // debug
                            this.requestStreamSubscription = sub
                            this.requestStreamSubscription.request(requestedMsg)
                        },
                        onNext: (e) => {
                        // this.surveyResponses.push(e.data)
                        // // handle incoming data, update series in the corresponding chart
                        // this.addSurveyResponseToSeries(e.data)
                        // // count processed messages, when the buffer is full, request more from the socket
                        // processedMsg++;
                        // if (processedMsg >= requestedMsg) {
                        //     this.requestStreamSubscription.request(requestedMsg);
                        //     processedMsg = 0;
                        // }
                        },
                        onError: error => {
                        // console.log("got error with requestStream"); // debug
                        console.error(error);
                        },
                        onComplete: () => {
                        // console.log("requestStream completed"); // debug
                        }
                    });
                },
                onError: error => {
                // console.log("got connection error"); // debug
                console.error(error);
                },
                // eslint-disable-next-line no-unused-vars
                onSubscribe: cancel => {
                /* call cancel() to abort */
                }
            });

        },

        methods:{

            // async createClient(options) {
            //     const client = new RSocketClient({
            //         setup: {
            //             dataMimeType: 'text/plain',
            //             keepAlive: 1000000, // avoid sending during test
            //             lifetime: 100000,
            //             metadataMimeType: 'text/plain',
            //         },
            //         transport: new RSocketWebsocketClient({
            //             host: options.host,
            //             port: options.port,
            //         }),
            //     });

            //     return client.connect();
            // },


            // rsocket stuff


                   
            async search(query) {
                var me = this;
                if(me.offline){
                    if(!me.values) me.values = [];
                    return;
                } 

                var temp = null;
                if(query!=null){
                    temp = await axios.get(axios.fixUrl('/vacations/' + query.apiPath), {params: query.parameters})
                }else{
                    temp = await axios.get(axios.fixUrl('/vacations'))
                }

                me.values = temp.data._embedded.vacations;
                
                me.newValue = {
                    'startDate': '2022-12-12',
                    'endDate': '2022-12-12',
                    'reason': '',
                    'userId': '',
                    'days': 0,
                    'status': '',
                }
            },

            closeDialog(){
                this.openDialog = false
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                
                this.$emit('input', this.values);

                this.$nextTick(function(){
                    this.tick=true
                })
            },
            remove(value){
                var where = -1;
                for(var i=0; i<this.values.length; i++){
                    if(this.values[i]._links.self.href == value._links.self.href){
                        where = i;
                        break;
                    }
                }

                if(where > -1){
                    this.values.splice(i, 1);
                    this.$emit('input', this.values);
                }
            },
        }
    };
</script>


<style>
    .video-card {
        width:300px; 
        margin-left:4.5%; 
        margin-top:50px; 
        margin-bottom:50px;
    }
</style>

