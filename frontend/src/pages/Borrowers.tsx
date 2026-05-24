import api from "../services/api";
import { useEffect, useState } from "react";
import Modal from "../components/Modal";
import UpdateBorrowerModal from "../components/UpdateBorrowerModal";
import CreateBorrowerModal from "../components/CreateBorrowerModal";
import BorrowerDetailModal from "../components/BorrowerDetailModal";
import { Book } from "./Books";
import SearchBar from "../components/SearchBar";

export interface Borrower {
  id : number;
  name: string;
  phone: string;
  address: string;
  status: string;
  email: string;
  borrowings:{
    borrowingId: number,
    bookId: number,
    borrowerId: number,
    borrowedDate: string,
    dueDate: string,
    returnedDate: string,
    status: string
  }[];
}


export default function Borrowers() {
  const [borrowerList, setBorrowerList] = useState<Borrower[]>([]);
  const [bookList, setBookList] = useState<Book[]>([]);
  const [isShowModalUpdate, setIsShowModalUpdate] = useState(false);
  const [isShowModalCreate, setIsShowModalCreate] = useState(false);
  const [isShowModalDetail, setIsShowModalDetail] = useState(false);
  const [currentBorrower, setCurrentBorrower] = useState<Borrower>();
  const [searchQuery, setSearchQuery] = useState('');

  const handleOpenModalDetail = async (borrower: any) => {
    try {
      const res = await api.get('/borrowers/' + borrower.id);
      if (res) {
          console.log(res.data);
          setCurrentBorrower(res.data);
          handleGetBooks();
      }
  } catch (error) {
      console.error(error);
  }
  setIsShowModalDetail(true);

  };

  const handleCloseModalDetail = () => {
    setIsShowModalDetail(false)
  }; 

const handleCloseModalUpdate = () => {
    setIsShowModalUpdate(false);
    
};
const handleOpenModalUpdate = (borrower:any) => {
  setIsShowModalUpdate(true);
  setCurrentBorrower(borrower);
     
};

const handleOpenModalCreate = () => {
  setIsShowModalCreate(true);
};

const handleCloseModalCreate = () => {
  setIsShowModalCreate(false);
};

const handleDelete = async (borrower: any) => {
  setCurrentBorrower(borrower);
  try {
      const res = await api.delete('/borrowers/' + borrower.id);
      if (res) {
          console.log(res.data);
          handleGetBorrowers();
      }
  } catch (error) {
      console.error(error);
  }
}

const handleSubmitCreate = async ({name,email,phone,address} : {name: string, email:string, phone: string, address: string}) => {

  try {
      const res = await api.post ('/borrowers', {
          name: name,
          email: email,
          phone: phone,
          address: address
      });

      if (res) {
          console.log(res.data);
          handleGetBorrowers();
          handleCloseModalCreate();
      }
  } catch (error) {
      console.error(error);
  }
};

const handleSubmitUpdate = async ({name,email,phone,address} : { name: string,email : String, phone: string,address: string}) => {
  try {
      const res = await api.patch ('/borrowers/' + currentBorrower!.id, {
          name: name,
          email:email,
          phone: phone,
          address: address
      })
      if (res) {
        console.log(res.data);
        handleGetBorrowers();
        handleCloseModalUpdate();
      }
    } catch (error) {
      console.error(error);
    }
};

const handleGetBorrowers = async () => {
  try {
    const res = await api.get('/borrowers');
    if (res.status) {
      setBorrowerList(res.data);
      console.log(res.data);
    }
  } catch (error) {
    console.log(error);
  }
};

const searchList = borrowerList.filter((borrower) => {
  const searchByName = borrower.name.toLowerCase().includes(searchQuery.toLowerCase());
  const searchById = borrower.id.toString().includes(searchQuery);
  return searchByName || searchById;
});

const handleGetBooks = async () => {  
  try {
      const res = await api.get ('/books');
      if (res.status === 200) {
          setBookList(res.data);
          console.log(res.data);
      }
  } catch (error) {
      console.error(error);
  }
};

useEffect(() => {
  handleGetBorrowers();
}, []);
  return (
    <div className="flex flex-col w-full h-screen p-4">
      <div className="flex flex-row justify-between items-center mb-4">
        <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} placeholder="Search by Borrower's name or id" />
        <button onClick={()=>handleOpenModalCreate()} className="absolute border-2 border-green-500 bg-green-500 text-white font-semibold p-2 hover:bg-white hover:text-black top-24 right-12 rounded-lg" >Add Borrower</button>
      </div>
      <div className="relative overflow-x-auto w-full">
          <table className="w-full  rtl:text-right text-gray-500 text-center">
              <thead className="text-base text-gray-700 uppercase bg-gray-100">
                  <tr>
                      <th className="px-6 py-3">
                          Borrower's ID
                      </th>
                      <th className="px-6 py-3">
                          Name
                      </th>
                      <th className="px-6 py-3">
                          Email
                      </th>
                      <th className="px-6 py-3">
                          Phone
                      </th>
                      <th className="px-6 py-3">
                          Status
                      </th>
                      <th className="px-6 py-3">
                          Address
                      </th>
                      <th className="px-6 py-3">
                          Action
                      </th>
                  </tr>
              </thead>
              <tbody>
                {searchList.map((borrower) => (
                  <tr key={borrower.id} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 text-base">
                      <th className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                          {borrower.id}
                      </th>
                      <td className="px-6 py-4">
                          {borrower.name}
                      </td>
                      <td className="px-6 py-4">
                          {borrower.email}
                      </td>
                      <td className="px-6 py-4">
                          {borrower.phone}
                      </td>
                      <td className="px-6 py-4">
                          {borrower.status}
                      </td>
                      <td className="px-6 py-4">
                          {borrower.address}
                      </td>
                      <td className="px-6 py-4 flex justify-center gap-4">
                          <button className=" w-20 border-2 border-blue-500 hover:bg-white hover:text-black p-2 font-semibold rounded-lg bg-blue-500 text-white" onClick={()=>handleOpenModalDetail(borrower)} >Check</button>
                          <button className=" w-20 border-2 border-yellow-500 hover:bg-white hover:text-black p-2 font-semibold rounded-lg bg-yellow-500 text-white" onClick={()=>handleOpenModalUpdate(borrower)} >Edit</button>
                          <button className="w-20 border-2 border-red-500 hover:bg-white hover:text-black p-2 font-semibold rounded-lg bg-red-500 text-white" onClick={()=>handleDelete(borrower)}>Delete</button>
                      </td>
                  </tr>
                ))}
              </tbody>
          </table>
      </div>
      <Modal isShowModal={isShowModalUpdate}>
        <UpdateBorrowerModal handleCloseModal={handleCloseModalUpdate} handleSubmit={handleSubmitUpdate} borrower={currentBorrower!}/>
      </Modal>
      
      <Modal isShowModal={isShowModalCreate}>
          <CreateBorrowerModal handleCloseModal={handleCloseModalCreate} handleSubmit={handleSubmitCreate}/>
      </Modal>

      <Modal isShowModal={isShowModalDetail}>
          <BorrowerDetailModal borrower={currentBorrower!} handleCloseModal={handleCloseModalDetail} bookList={bookList}/>
      </Modal>
    </div>
  )
}
