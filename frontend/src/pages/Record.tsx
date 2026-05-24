import { useEffect, useState } from "react";
import { Book } from "./Books";
import api from "../services/api";
import Modal from "../components/Modal";
import ReturnBookModal from "../components/ReturnBookModal";
import SearchBar from "../components/SearchBar";

export interface Record {
  borrowingId: number,
  bookId: number,
  borrowerId: number,
  borrowedDate: string,
  dueDate: string,
  returnedDate: string,
  status: string
}

export default function Record() {

  const [bookList, setBookList] = useState<Book[]>([]);
  const [recordList, setRecordList] = useState<Record[]>([]);
  const [isShowModalReturn, setIsShowModalReturn] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<Record>();
  const [searchQuery, setSearchQuery] = useState<string>('');

  const handleGetBooks = async () => {
    try {
            const res = await api.get ('/books');
            if (res.status) {
                setBookList(res.data);
                console.log(res.data);
            }
        } catch (error) {
            console.error(error);
        }
  };

  const handleGetRecords = async () => {
    try {
      const res = await api.get('/borrowings');
      if (res.status) {
        setRecordList(res.data);
        console.log(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const searchList = recordList.filter((record) => {
    const searchByTitle = bookList.find(book => book.bookId === record.bookId)?.title.toLowerCase().includes(searchQuery.toLowerCase());
    const searchByBorrowerId = record.borrowerId.toString().includes(searchQuery);
    return searchByTitle || searchByBorrowerId;
  });

  const handleOpenModalReturn = (record : Record) => {
    setCurrentRecord(record);
    setIsShowModalReturn(true);
  }

  const handleCloseModalReturn = () => {
    setIsShowModalReturn(false);
  };

  const handleReturn = async (date: string) => {
    const updatedRecord = {...currentRecord, returnedDate: date, status: "RETURNED"};
    console.log(updatedRecord);
    
    try {
      const res = await api.put('/borrowings/' + currentRecord?.borrowingId, updatedRecord);
      if (res.status) {
        console.log(res.data);
        handleGetRecords();
        handleCloseModalReturn();
      }
    } catch (error) {
      console.log(error);
    }
  };


  useEffect(() => {
    handleGetBooks();
    handleGetRecords();
  }, []);

  return (
    <div className="flex flex-col w-full h-screen p-4">
      <div className="mb-4">
        <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} placeholder="Search by Book Title or Borrower's ID"/>
      </div>
      <div className="relative overflow-x-auto w-full">
          <table className="w-full text-left rtl:text-right text-gray-500">
              <thead className="text-base text-gray-700 uppercase bg-gray-100">
                  <tr>
                      <th className="px-6 py-3">
                          Book Title
                      </th>
                      <th className="px-6 py-3">
                          Borrower's ID
                      </th>
                      <th className="px-6 py-3">
                          Borrow Date
                      </th>
                      <th className="px-6 py-3">
                          Due Date
                      </th>
                      <th className="px-6 py-3">
                          Return Date
                      </th>
                      <th className="px-6 py-3">
                          Status
                      </th>
                      <th className="px-6 py-3">
                          Action
                      </th>
                  </tr>
              </thead>
              <tbody>
                {searchList.map((record) => (
                  <tr key={record.borrowingId} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 text-base">
                      <th className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                          {bookList.find(book => book.bookId === record.bookId)?.title}
                      </th>
                      <td className="px-6 py-4">
                          {record.borrowerId}
                      </td>
                      <td className="px-6 py-4">
                          {record.borrowedDate}
                      </td>
                      <td className="px-6 py-4">
                          {record.dueDate}
                      </td>
                      <td className="px-6 py-4">
                          {record.returnedDate || "None"}
                      </td>
                      <td className="px-6 py-4">
                          {record.status}
                      </td>
                      <td className="px-6 py-4">
                          <button className={`w-20 border-2 border-blue-500 p-2 text-base font-semibold rounded-lg bg-blue-500 text-white ${record.status != "RETURNED" && "hover:bg-white hover:text-black"}  ${record.status == "RETURNED" && "cursor-not-allowed opacity-50"}`} disabled = {record.status == "RETURNED"? true:false}onClick={() => handleOpenModalReturn(record)}>Return</button>
                      </td>
                  </tr>
                ))}
              </tbody>
          </table>
      </div>

      <Modal isShowModal={isShowModalReturn}>
        <ReturnBookModal handleCloseModal={handleCloseModalReturn} handleSubmit={handleReturn}/>
      </Modal>

    </div>
  )
}
