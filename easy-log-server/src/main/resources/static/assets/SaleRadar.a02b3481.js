import{a as i,w as r,x as s,o as l,j as n,H as d,i as m,bs as u,k as f}from"./index.c2fb29d9.js";import{C as c}from"./index.d535e1dd.js";import"./index.a038765d.js";import"./index.140cefd0.js";import{u as h}from"./useECharts.3ef6dc7a.js";import"./index.de20b915.js";const x=i({props:{loading:Boolean,width:{type:String,default:"100%"},height:{type:String,default:"400px"}},setup(e){const a=e,t=r(null),{setOptions:o}=h(t);return s(()=>a.loading,()=>{a.loading||o({legend:{bottom:0,data:["Visits","Sales"]},tooltip:{},radar:{radius:"60%",splitNumber:8,indicator:[{name:"2017"},{name:"2017"},{name:"2018"},{name:"2019"},{name:"2020"},{name:"2021"}]},series:[{type:"radar",symbolSize:0,areaStyle:{shadowBlur:0,shadowColor:"rgba(0,0,0,.2)",shadowOffsetX:0,shadowOffsetY:10,opacity:1},data:[{value:[90,50,86,40,50,20],name:"Visits",itemStyle:{color:"#b6a2de"}},{value:[70,75,70,76,20,85],name:"Sales",itemStyle:{color:"#67e0e3"}}]}]})},{immediate:!0}),(p,g)=>(l(),n(f(c),{title:"\u9500\u552E\u7EDF\u8BA1",loading:e.loading},{default:d(()=>[m("div",{ref_key:"chartRef",ref:t,style:u({width:e.width,height:e.height})},null,4)]),_:1},8,["loading"]))}});export{x as default};
