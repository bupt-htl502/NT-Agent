import { saveAs } from "file-saver";

export const blob = (response: any, defaultFileName: string = "1.bin") => {
    //根据响应头获取文件名称
    let fileName = response.headers['content-disposition'].match(/filename=(.*)/)[1]
    fileName = fileName ? decodeURI(fileName) : defaultFileName;
    const blob = new Blob([response.data], { type: 'application/octet-stream' })
    return {
        'blob': blob,
        "filename": fileName
    }
}

export const save = (response: any, defaultFileName: string = "1.bin") => {
    //根据响应头获取文件名称
    let fileName = response.headers['content-disposition'].match(/filename=(.*)/)[1]
    fileName = fileName ? decodeURI(fileName) : defaultFileName;
    const blob = new Blob([response.data], { type: 'application/octet-stream' })
    saveAs(blob, fileName);

}
