import React from "react";

const FileCard = (props) => {
    let filename = props.filename;
    const folderimg = "https://icons.iconarchive.com/icons/dtafalonso/yosemite-flat/512/Folder-icon.png"
    const imageimg = "https://cdn-icons-png.freepik.com/256/15147/15147765.png?semt=ais_hybrid"
    const excelimg = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Microsoft_Excel_2013-2019_logo.svg/1200px-Microsoft_Excel_2013-2019_logo.svg.png"
    const pdfimg = "https://www.freeiconspng.com/uploads/pdf-icon-symbol-5.png"
    let url;
    if (typeof filename === "string") {
        if (filename.includes(".jpg")  || filename.includes(".png")){
            url = imageimg;
        }
        else if (filename.includes(".xlsx") ){
            url = excelimg;
        }
        else if (filename.includes(".pdf") ){
            url = pdfimg;
        }
        else{
            url = folderimg;
        }
        filename = filename.substring(0, 20);
    }
    return (
    <div className=" inline-block w-44 m-5">
        <img className="w-44 py-0 " src={url} alt="logo" />
        <h2 className=" text-center overflow-hidden font-bold">{filename}</h2>
    </div>
  );
};

export default FileCard;
