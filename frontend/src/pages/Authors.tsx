import api from "../services/api";
import { useEffect, useState } from "react";
import Modal from "../components/Modal";
import AuthorCard from "../components/AuthorCard";
import CreateAuthorModal from "../components/CreateAuthorModal";
import UpdateAuthorModal from "../components/UpdateAuthorModal";
import AuthorDetailModal from "../components/AuthorDetailModal";
import SearchBar from "../components/SearchBar";

export interface Author {
  authorId : number,
  name: string,
  nationality: string,
  portraitUrl: string,
  books: {
    id: number,
    title: string,
    isbn: string,
    status: string,
  }[];
}

export default function Authors() {
  const [authorList, setAuthorList] = useState([]);
  const [isShowModalCreate, setIsShowModalCreate] = useState(false);
  const [isShowModalUpdate, setIsShowModalUpdate] = useState(false);
  const [isShowModalDetail, setIsShowModalDetail] = useState(false); // New state
  const [currentAuthor, setCurrentAuthor] = useState<Author>();
  const [searchQuery, setSearchQuery] = useState<string>("");

  const handleOpenModalDetail = (author: any) => {
    setCurrentAuthor(author);
    setIsShowModalDetail(true);
  };

  const handleCloseModalDetail = () => setIsShowModalDetail(false);

  const handleGetAuthors = async () => {
    try {
      const res = await api.get("/authors");
      if (res.status === 200) {
        setAuthorList(res.data);
        console.log(res.data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const searchList = authorList.filter((author: Author) => {
    const searchByName = author.name.toLowerCase().includes(searchQuery.toLowerCase());
    return searchByName;
  });

  const handleOpenModalCreate = () => {
    setIsShowModalCreate(true);
  };

  const handleCloseModalCreate = () => {
    setIsShowModalCreate(false);
  };

  const handleOpenModalUpdate = (author: any) => {
    setIsShowModalUpdate(true);
    setCurrentAuthor(author);
  };

  const handleCloseModalUpdate = () => {
    setIsShowModalUpdate(false);
  };

  const handleSubmitCreate = async ({
    name,
    nationality,
    portraitUrl,
  }: {
    name: string;
    nationality: string;
    portraitUrl: string;
  }) => {
    try {
      const res = await api.post("/authors", {
        name,
        nationality,
        portraitUrl,
      });

      if (res) {
        console.log(res.data);
        handleGetAuthors();
        handleCloseModalCreate();
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleSubmitUpdate = async ({
    authorId,
    name,
    nationality,
    portraitUrl,
    books,
  }: Author) => {
    const updatedAuthor = {
      authorId,
      name,
      nationality,
      portraitUrl,
      books,
    };
    try {
      const res = await api.put(
        "/authors/" + currentAuthor?.authorId,
        updatedAuthor
      );

      if (res) {
        console.log(res.data);
        handleGetAuthors();
        handleCloseModalUpdate();
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const res = await api.delete("/authors/" + id);
      if (res) {
        console.log(res.data);
        handleGetAuthors();
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    handleGetAuthors();
  }, []);

  return (
    <div className="p-4">
      <div className="flex flex-row justify-between items-center px-4">
        <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} placeholder="Search by Author's name" />
        <button
          className="border-2 border-green-500 bg-green-500 text-white font-semibold p-2 hover:bg-white hover:text-black rounded-lg"
          onClick={() => handleOpenModalCreate()}
          >
          Add Author
        </button>
      </div>
      <div className="flex flex-row flex-wrap w-full h-fit gap-6 mt-4 justify-center">
        {searchList.map((author: any) => (
          <AuthorCard
            key={author.authorId}
            author={author}
            handleOpenModalUpdate={handleOpenModalUpdate}
            handleDelete={handleDelete} 
            handleOpenAuthorDetail={handleOpenModalDetail}/>
        ))}
      </div>

      {/* Modal for creating author */}
      <Modal isShowModal={isShowModalCreate}>
        <CreateAuthorModal
          handleCloseModal={handleCloseModalCreate}
          handleSubmit={handleSubmitCreate}
        />
      </Modal>

      {/* Modal for updating author */}
      <Modal isShowModal={isShowModalUpdate}>
        <UpdateAuthorModal
          handleCloseModal={handleCloseModalUpdate}
          handleSubmit={handleSubmitUpdate}
          author={currentAuthor!}
        />
      </Modal>

      {/* Modal for displaying author details */}
      <Modal isShowModal={isShowModalDetail}>
        <AuthorDetailModal handleCloseModal={handleCloseModalDetail} author={currentAuthor!} />
      </Modal>
    </div>
  );
}
